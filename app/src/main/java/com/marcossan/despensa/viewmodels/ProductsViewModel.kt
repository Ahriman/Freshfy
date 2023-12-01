package com.marcossan.despensa.viewmodels

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

}