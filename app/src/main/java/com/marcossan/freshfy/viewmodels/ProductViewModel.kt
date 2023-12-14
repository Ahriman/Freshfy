package com.marcossan.freshfy.viewmodels

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import androidx.compose.material.Snackbar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.gson.stream.JsonReader
import com.marcossan.freshfy.FreshfyApp
import com.marcossan.freshfy.MainActivity
import com.marcossan.freshfy.R
import com.marcossan.freshfy.data.model.Product
import com.marcossan.freshfy.states.ProductsState
import com.marcossan.freshfy.data.network.ProductApiService
import com.marcossan.freshfy.data.network.ProductJson
import com.marcossan.freshfy.data.local.ProductRepository
import com.marcossan.freshfy.states.NotificationState
import com.marcossan.freshfy.states.ProductState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.MissingFieldException
import java.io.StringReader
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

sealed interface ScannerUiState {
    data class Success(val product: Product) : ScannerUiState
    object Error : ScannerUiState
    object Loading : ScannerUiState
}

/**
 * Encargado de conectar las peticiones a la base de datos y actualizar las vistas
 */
@HiltViewModel
class ProductViewModel @Inject constructor(
//    private val dao: ProductsDatabaseDao
    private val productRepository: ProductRepository
) : ViewModel() {

    // ROOM start

    var state by mutableStateOf(ProductsState())
        private set

    var productState by mutableStateOf(ProductState())
        private set

    // Comportamiento cuándo se inicie el ViewModel
    init {
        viewModelScope.launch {
            productRepository.getAll().collectLatest {
                state = state.copy(
                    products = it
                )
            }
        }
    }

    @Throws(Exception::class)
    fun addProduct(product: Product) = viewModelScope.launch {
        println("product = $product") // TODO QUITAR
        productRepository.addProduct(product)
    }

    fun updateProduct(product: Product) = viewModelScope.launch {
        productRepository.updateProduct(product)
        clearData()
    }

    fun deleteProduct(product: Product) = viewModelScope.launch {
        productRepository.deleteProduct(product)
    }

    fun getProduct(barcode: String) = viewModelScope.launch {
        val e = productRepository.getProduct(barcode)
        e.collectLatest { product ->
            _product = product
        }


        viewModelScope.launch {
            productRepository.getProduct(barcode).collectLatest {
                productState = productState.copy(
                    product = it
                )
            }
        }
    }


    fun scheduleNotification(days: Int, context: Context) = viewModelScope.launch {

        // Pasar días a milisegundos
        // Obtener fecha actual en ms
        // Obtener direfencia y con ella buscar en BDD con getProductsThatExpiredInDays

        productRepository.getProductsThatExpiredInDays(days)

//
//
//        val tiempoRestante = calcularTiempoRestante(fecha.fecha)
//
//        if (tiempoRestante > 0) {
//            // Configura y muestra la notificación utilizando el servicio de notificaciones de Android
//            // Puedes utilizar NotificationManager y otras clases según tus necesidades
//        }



//        val notificationManager = context.getSystemService(NotificationManager::class.java)
//        val notification = Notification.Builder(context, FreshfyApp.CHANEL_ID)
//            .setContentTitle(state.name)
//            .setContentText("Esto es una notificación")
//            .setSmallIcon(R.drawable.logo_notificacion)
//            .setAutoCancel(true)
//            .build()
//        notificationManager.notify(state.name.hashCode(), notification)





    }
    // Falta solicitar permisos al usuario para que funcione
    // TODO: https://www.youtube.com/watch?v=7RUCSOsp2jQ
    // https://www.youtube.com/watch?v=imFJZ4Kbv_g

    fun sendNotificacion(context: Context) {

//        val navcontroller = NavController(context = context)
//        navcontroller.graph.apply {
//            addNode
//        }

        // TODO: Hacer que vaya a la pantalla del producto notificado
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT
            PendingIntent.FLAG_IMMUTABLE
        )


        val notificationManager = context.getSystemService(NotificationManager::class.java)
        val notification = Notification.Builder(context, FreshfyApp.CHANNEL_ID)
            .setContentTitle(notificationState.name)
            .setContentText("A tu producto [nombre_producto] le quedan [X] días para caducar. eer" +notificationState.name.hashCode())
            .setSmallIcon(R.drawable.logo_notificacion)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // Permite que la notifiación sea deslizable
            .build()

        // TODO: Agrupar notificaciones por producto
        notificationManager.notify(notificationState.name.hashCode(), notification) //notificationState.name.hashCode()

    }

    var notificationState by mutableStateOf(NotificationState())
        private set

    fun changeName(text: String) {
        notificationState = notificationState.copy(
            name = text
        )
    }

    // ROOM end


//    suspend fun getProductByBarcode(barcode: String): Product? {
//        return state.products.find { it.code == barcode }
//    }

    // Function to get product by barcode
    suspend fun getProductByBarcode(barcode: String): Flow<Product> {
        return productRepository.getProduct(barcode)
    }

    suspend fun getProductByBarcode2(barcode: String): Product {
        return productRepository.getProduct2(barcode)
    }


    private fun clearData() {
        _barcode = ""
        _productName = ""
        _productUrl = ""
        _productExpireDate = ""
        _productQuantity = ""

        _product = Product(
            code = _barcode,
            name = _productName,
            imageUrl = _productUrl,
            expirationDate = _productExpireDate,
            quantity = _productQuantity
        )
    }

    var scannerUiState: ScannerUiState by mutableStateOf(ScannerUiState.Loading)
        private set

    suspend fun getProductFromApi(barcode: String) {
        val productJson: ProductJson
        try {
            productJson = ProductApiService.OpenFoodFactsApi.retrofitService.getProduct(
                barcode = barcode ?: _barcode
            )
            val product = getProductFromProductJson(productJson)
            _productName = product.name
            _productUrl = product.imageUrl
        } catch (e: MissingFieldException) {
            // TODO: Crear ventana modal, emergente?
            println("El código de barras no es válido o no existe en la base de datos porque el Json devuelto no es correcto.")
        }
    }

    private var _barcode by mutableStateOf("")
    val barcode get() = _barcode

    private var _productName by mutableStateOf("")
    val productName get() = _productName

    private var _productUrl by mutableStateOf("")
    val productUrl get() = _productUrl


    private var _productExpireDate by mutableStateOf("")
    val productExpireDate get() = _productExpireDate

    private var _productQuantity by mutableStateOf("")
    val productQuantity get() = _productQuantity


    private var _isBarcodeScanned by mutableStateOf(false)
    val isBarcodeScanned get() = _isBarcodeScanned

    private var _isValidProductQuantity by mutableStateOf(false)
    val isValidProductQuantity get() = _isValidProductQuantity


    fun onProductChange(product: MutableStateFlow<Product?>) {
        _productFlow = product
    }

//    fun onProductChange(barcode: Flow<Product>?) {
//
//        viewModelScope.launch {
//            productRepository.getProduct(barcode).collectLatest {
////                state = state.copy(
////                    product = it
////                )
//                _product = _product.copy(
//                    id = it.id,
//                    code = it.code,
//                    name = it.name,
//                    imageUrl = it.imageUrl,
//                    expirationDate = it.expirationDate,
//                    dateAdded = it.dateAdded,
//                    quantity = it.quantity
//                )
//            }
//        }
//    }


    fun onBarcodeChange(barcode: String) {
        _barcode = barcode
    }

    fun onProductNameChange(productName: String) {
        _productName = productName.replaceFirstChar { it.uppercase() }
    }

    fun onScannedBarcode(barcode: String) {
        _barcode = barcode
        _isBarcodeScanned = true
    }

    fun onProductExpireDateChange(productExpireDate: String) {
        _productExpireDate = productExpireDate
    }

    fun onProductQuantityChange(productQuantity: String) {
        _productQuantity = productQuantity
    }

    private var _product by mutableStateOf(
        Product(
            code = _barcode,
            name = _productName,
            imageUrl = _productUrl,
            expirationDate = _productExpireDate,
            quantity = _productQuantity
        )
    )

    val product get() = _product

    private var _productFlow = MutableStateFlow<Product?>(null)
    val productFlow get() = _productFlow.asStateFlow()

    fun setIsBarcodeScanned(enable: Boolean) {
        _isBarcodeScanned = enable
    }

    /**
     * Gets Product information from the Open Food Fatcs API Retrofit service and updates the
     * [Product] [List] [MutableList].
     */
    fun getProductFromApi(barcode: String?): Product {

        viewModelScope.launch {
            scannerUiState =
                try {
                    val productJson: ProductJson =
                        ProductApiService.OpenFoodFactsApi.retrofitService.getProduct(
                            barcode = barcode ?: _barcode
                        )
                    _product = getProductFromProductJson(productJson = productJson)
//                    listaProductos.add(product)
//                    if (barcode.isNullOrEmpty()) {
//                        listaProductos.add(product)
//                    }network


                    ScannerUiState.Success(
                        _product
                    )

                } catch (e: Exception) {
                    e.printStackTrace()

                    ScannerUiState.Error
                }

        }

        return _product
    }

    private fun getProductFromProductJson(productJson: ProductJson): Product {
        val barcode = productJson.code
        var nombreProducto = ""
        var imageURL = ""
        var fechaCaducidad = ""

        val stringReader = StringReader(productJson.product.toString())
        val jsonReader = JsonReader(stringReader)

        jsonReader.beginObject() // Start reading the JSON object
        while (jsonReader.hasNext()) {
            when (jsonReader.nextName()) {
                "product_name_es" -> {
                    val value = jsonReader.nextString()
                    println("product_name_es: $value")
                    nombreProducto = value
                }

                "image_front_url" -> {
                    val value = jsonReader.nextString()
                    println("image_front_url: $value")
                    imageURL = value
                }

                "expiration_date" -> {
                    val value = jsonReader.nextString()
                    println("Fecha de caducidad: $value")
                    fechaCaducidad = value
                    if (value.isEmpty()) fechaCaducidad = "No disponible"
                }

                else -> jsonReader.skipValue() // Handle unexpected fields
            }
        }
        jsonReader.endObject() // End of the JSON object

        jsonReader.close() // Close the JsonReader when done

        return Product(
            code = barcode,
            name = nombreProducto,
            imageUrl = imageURL,
//            expirationDate = fechaCaducidad,
            expirationDate = _productExpireDate,
            dateAdded = calcularFechaActual()
        )
    }

    private fun calcularFechaActual(): String {
        val fecha = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss")
//    println("dd/MM/yy HH:mm:ss: " + fecha.format(LocalDateTime.now()))
        return fecha.format(LocalDateTime.now())
    }

    @SuppressLint("SimpleDateFormat")
    fun validarFormatoFecha(): Boolean {
        val formato = SimpleDateFormat("dd/MM/yyyy")
        formato.isLenient = false

        try {
            formato.parse(_productExpireDate)
            return true
        } catch (e: ParseException) {
            return false
        }
    }

    // Métodos para notificaciones
//    fun programarNotificacion(days: Int) {
//
//        productRepository.getProductsThatExpiredInDays(days)
//
//        val tiempoRestante = calcularTiempoRestante(fecha.fecha)
//
//        if (tiempoRestante > 0) {
//            // Configura y muestra la notificación utilizando el servicio de notificaciones de Android
//            // Puedes utilizar NotificationManager y otras clases según tus necesidades
//        }
//    }

    private fun calcularTiempoRestante(fechaMillis: Long): Long {
        // Calcula el tiempo restante hasta la fecha en milisegundos
        val tiempoActual = System.currentTimeMillis()
        return fechaMillis - tiempoActual
    }

}

//@Suppress("UNCHECKED_CAST")
//class ProductsViewModelFactory(
//    private val dao: ProductsDatabaseDao
//) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return ProductViewModel(dao = dao) as T
//    }
//}

//@Suppress("UNCHECKED_CAST")
//class ProductsViewModelFactory(
//    private val productRepository: ProductRepository
//) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return ProductViewModel(productRepository = ProductRepository) as T
//    }
//}