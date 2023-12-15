package com.marcossan.freshfy.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcossan.freshfy.data.local.ProductRepository
import com.marcossan.freshfy.data.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    private var _product by mutableStateOf(Product(0, "", "", "", 0L, "", "", ""))
    val product get() = _product

//    MutableStateFlow<Product?>(null)

    fun getProduct(barcode: String): Product {
        viewModelScope.launch {
             _product = productRepository.getProduct(barcode)
            saveProductChanges()
        }
        return _product
    }

    suspend fun saveProductChanges() {
        _product.let {
            productRepository.updateProduct(it)
        }
    }

}