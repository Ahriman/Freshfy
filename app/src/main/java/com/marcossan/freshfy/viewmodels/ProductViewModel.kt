package com.marcossan.freshfy.viewmodels

import android.Manifest
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.stream.JsonReader
import com.marcossan.freshfy.FreshfyApp
import com.marcossan.freshfy.MainActivity
import com.marcossan.freshfy.R
import com.marcossan.freshfy.data.local.ProductRepository
import com.marcossan.freshfy.data.model.Product
import com.marcossan.freshfy.data.network.ProductApiService
import com.marcossan.freshfy.data.network.ProductJson
import com.marcossan.freshfy.states.NotificationState
import com.marcossan.freshfy.states.ProductsState
import com.marcossan.freshfy.utils.NotificationReceiver
import com.marcossan.freshfy.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import java.io.StringReader
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

const val NOTIFICATION_ID_EXTRA = "notification_id"

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

    // LiveData que emite la lista de productos como State
    val allProducts = productRepository.getAllProductsAsState()

    // ROOM start

    var state by mutableStateOf(ProductsState())
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
        val newNotificationId = generateNotificationId(product)
        product.notificationId = newNotificationId

        // Agregar el producto a Room
        productRepository.addProduct(product)

        // Programar notificación
        scheduleNotification(product, newNotificationId)
    }

    fun updateProduct(product: Product) = viewModelScope.launch {
        productRepository.updateProduct(product)
//        clearData() // TODO
    }

    fun deleteProduct(product: Product) = viewModelScope.launch {
        productRepository.deleteProduct(product)
    }

    fun getProductById(productId: Long): LiveData<Product> {
        return productRepository.getProductById(productId)
    }

    // ROOM end

    // Notifications start
    var notificationState by mutableStateOf(NotificationState())
        private set

    fun changeName(text: String) {
        notificationState = notificationState.copy(
            name = text
        )
    }

//    fun setProductId(productId: String) {
//        _barcode = productId
//    }
//
//    val producto: LiveData<Product?> = Transformations.switchMap(_productId) { productId ->
//        productRepository.getProduct(productId)
//    }

    //    fun scheduleNotification(context: Context, notificationId: Int, delayMillis: Long) {
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val intent = Intent(context, NotificationReceiver::class.java)
//            .setAction("com.marcossan.freshfy.ACTION_SHOW_NOTIFICATION")
//            .putExtra(NOTIFICATION_ID_EXTRA, notificationId)
//
//        val pendingIntent = PendingIntent.getBroadcast(
//            context,
//            notificationId,
//            intent,
////            PendingIntent.FLAG_ONE_SHOT
//            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        // Programar la acción del BroadcastReceiver para ejecutarse en el futuro
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delayMillis, pendingIntent)
//    }

    fun scheduleNotification(context: Context, notificationId: Int, delayMillis: Long) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.SCHEDULE_EXACT_ALARM
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // El permiso para programar alarmas exactas está concedido
            scheduleExactAlarm(context, notificationId, delayMillis)
        } else {
            // El permiso no está concedido, solicita el permiso o maneja la situación de otra manera
            viewModelScope.launch {
                // Ejemplo: solicitar permiso al usuario (debe implementarse según tus necesidades)
                requestScheduleExactAlarmPermission(context)
            }
        }
    }

    private fun scheduleExactAlarm(context: Context, notificationId: Int, delayMillis: Long) {
        val alarmManager = context.getSystemService<AlarmManager>()
        val intent = Intent(context, NotificationReceiver::class.java)
            .setAction("com.example.ACTION_SHOW_NOTIFICATION")
            .putExtra(NOTIFICATION_ID_EXTRA, notificationId)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        // Programar la acción del BroadcastReceiver para ejecutarse en el futuro
        alarmManager?.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delayMillis, pendingIntent)
    }

    private suspend fun requestScheduleExactAlarmPermission(context: Context) {
        // Ejemplo: implementar lógica para solicitar permiso al usuario
        // (puede ser mediante un diálogo, una actividad, etc.)
    }

    fun checkAndScheduleNotification(context: Context, product: Product) {
        if (isExpirationNear(Date(product.expirationDate))) {
            val notificationId = generateNotificationId(product)
            scheduleNotification(context, notificationId, product.expirationDate)
        }
    }

    private fun isExpirationNear(expiryDate: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = expiryDate
        calendar.add(Calendar.DAY_OF_YEAR, -5) // Restar 5 días

        return System.currentTimeMillis() >= calendar.timeInMillis
    }

    private fun generateNotificationId(product: Product): Int {
        return product.name.hashCode()
    }

    private fun scheduleNotification(product: Product, notificationId: Int) {
        // Lógica para programar la notificación, por ejemplo, usando AlarmManager
        // ...
        println("Notificación para el producto: $product")
    }

    fun scheduleNotification(days: Long, context: Context) = viewModelScope.launch {

        // Pasar días a milisegundos
        // Obtener fecha actual en ms
        // Obtener direfencia y con ella buscar en BDD con getProductsThatExpiredInDays

        val a = productRepository.getProductsThatExpiredInDays(days)

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


        sendNotificacion(context = context, product = product)

    }

    fun sendNotificacion(context: Context, product: Product) {

        // TODO: Hacer que vaya a la pantalla del producto notificado
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT
            PendingIntent.FLAG_IMMUTABLE
        )

        val daysToExpire = Utils.calculateDaysUntilExpiration(product.expirationDate)

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        val notification = Notification.Builder(context, FreshfyApp.CHANNEL_ID)
            .setContentTitle(notificationState.name)
            .setContentText("A tu producto ${product.name} le quedan $daysToExpire días para caducar.")
            .setSmallIcon(R.drawable.logo_notificacion)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // Permite que la notifiación sea deslizable
            .build()

        notificationManager.notify(
            product.name.hashCode(),
            notification
        )
    }
    // Notifications end

    fun clearData() {
        _barcode = ""
        _productName = ""
        _productUrl = ""
        _productExpireDate = ""
        _productQuantity = ""

        _product = Product(
            barcode = _barcode,
            name = _productName,
            imageUrl = _productUrl,
            expirationDate = Utils.getTimeMillisOfStringDate(_productExpireDate),
            expirationDateInString = _productExpireDate,
            quantity = _productQuantity
        )
    }

    var scannerUiState: ScannerUiState by mutableStateOf(ScannerUiState.Loading)
        private set

    @OptIn(ExperimentalSerializationApi::class)
    suspend fun getProductFromApi(barcode: String) {
        val productJson: ProductJson
        try {
            productJson = ProductApiService.OpenFoodFactsApi.retrofitService.getProduct(
                barcode = barcode
            )
            val product = getProductFromProductJson(productJson)
            _productName = product.name
            _productUrl = product.imageUrl
        } catch (e: MissingFieldException) {
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

    private var _productQuantity by mutableStateOf("1")
    val productQuantity get() = _productQuantity


    private var _isBarcodeScanned by mutableStateOf(false)
    val isBarcodeScanned get() = _isBarcodeScanned


    fun onProductChange(product: Product) {
        _product = product
    }

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
            barcode = _barcode,
            name = _productName,
            imageUrl = _productUrl,
            expirationDate = Utils.getTimeMillisOfStringDate(_productExpireDate),
            expirationDateInString = _productExpireDate,
            quantity = _productQuantity
        )
    )

    val product get() = _product

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

        val stringReader = StringReader(productJson.product.toString())
        val jsonReader = JsonReader(stringReader)

        jsonReader.beginObject() // Start reading the JSON object
        while (jsonReader.hasNext()) {
            when (jsonReader.nextName()) {
                "product_name_es" -> {
                    val value = jsonReader.nextString()
                    nombreProducto = value
                }

                "image_front_url" -> {
                    val value = jsonReader.nextString()
                    imageURL = value
                }

                else -> jsonReader.skipValue() // Handle unexpected fields
            }
        }
        jsonReader.endObject() // End of the JSON object

        jsonReader.close() // Close the JsonReader when done

        return Product(
            barcode = barcode,
            name = nombreProducto,
            imageUrl = imageURL,
            expirationDate = Utils.getTimeMillisOfStringDate(_productExpireDate),
            expirationDateInString = _productExpireDate,
            dateAdded = System.currentTimeMillis(),
            dateAddedInString = Utils.getStringDateFromMillis(System.currentTimeMillis())
        )
    }

}
