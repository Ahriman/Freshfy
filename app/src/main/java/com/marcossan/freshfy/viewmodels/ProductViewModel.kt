package com.marcossan.freshfy.viewmodels

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.stream.JsonReader
import com.marcossan.freshfy.data.model.Product
import com.marcossan.freshfy.states.ProductsState
import com.marcossan.freshfy.data.network.ProductApiService
import com.marcossan.freshfy.data.network.ProductJson
import com.marcossan.freshfy.data.local.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
): ViewModel()  {

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

    fun addProduct(product: Product) = viewModelScope.launch {
        println("product = $product")
        productRepository.addProduct(product)
    }

    fun updateProduct(product: Product) = viewModelScope.launch {
        productRepository.updateProduct(product)
    }

    fun deleteProduct(product: Product) = viewModelScope.launch {
        productRepository.deleteProduct(product)
    }

    // ROOM end


    var scannerUiState: ScannerUiState by mutableStateOf(ScannerUiState.Loading)
        private set

    suspend fun getProductFromApi(barcode: String) {
        val productJson: ProductJson
        try {
            productJson= ProductApiService.OpenFoodFactsApi.retrofitService.getProduct(
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
//        _productExpireDate = productExpireDate



//        val dateExpired: String
//        productExpireDate.let {
//            val localDate = Instant.ofEpochMilli(it ?: 0).atZone(ZoneId.of("UTC")).toLocalDate()
//            dateExpired = "${localDate.dayOfMonth}/${localDate.monthValue}/${localDate.year}"
//
//        }
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

    fun setIsBarcodeScanned(enable: Boolean) {
        _isBarcodeScanned = enable
    }

    /**
     * Gets Product information from the Open Food Fatcs API Retrofit service and updates the
     * [Product] [List] [MutableList].
     */
    fun getProduct(barcode: String?): Product {

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