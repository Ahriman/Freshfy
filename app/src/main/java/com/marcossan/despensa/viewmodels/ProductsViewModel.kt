package com.marcossan.despensa.viewmodels

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcossan.despensa.models.Product
import com.marcossan.despensa.room.ProductsDatabaseDao
import com.marcossan.despensa.states.ProductsState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat

/**
 * Encargado de conectar las peticiones a la base de datos y actualizar las vistas
 */
class ProductsViewModel(
    private val dao: ProductsDatabaseDao
): ViewModel()  {

    // ROOM start

    var state by mutableStateOf(ProductsState())
        private set

    // Comportamiento cuándo se inicie el ViewModel
    init {
        viewModelScope.launch {
            dao.getAll().collectLatest {
                state = state.copy(
                    products = it
                )
            }
        }
    }

    fun addProduct(product: Product) = viewModelScope.launch {
        dao.addProduct(product)
    }

    fun updateProduct(product: Product) = viewModelScope.launch {
        dao.updateProduct(product)
    }

    fun deleteProduct(product: Product) = viewModelScope.launch {
        dao.deleteProduct(product)
    }

    // ROOM end


    private var _barcode by mutableStateOf("")
    val barcode get() = _barcode


    private var _productExpireDate by mutableStateOf("")
    val productExpireDate get() = _productExpireDate

    private var _productQuantity by mutableStateOf("")
    val productQuantity get() = _productQuantity



    private var _isBarcodeScanned by mutableStateOf(false)
    val isBarcodeScanned get() = _isBarcodeScanned

    private var _isValidProductQuantity by mutableStateOf(false)
    val isValidProductQuantity get() = _isValidProductQuantity


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