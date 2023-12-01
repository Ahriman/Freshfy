package com.marcossan.despensa.states

import com.marcossan.despensa.models.Product

/**
 * Son los elementos que forman parte de los estados y a su vez, cuándo estos elementos reciban
 * modificaciones por parte del usuario, se redibujará la IU.
 */
data class ProductsState(
    val products: List<Product> = emptyList()
)
