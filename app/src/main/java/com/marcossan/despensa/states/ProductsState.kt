package com.marcossan.despensa.states

import com.marcossan.despensa.models.Product

data class ProductsState(
    val products: List<Product> = emptyList()
)
