package com.marcossan.freshfy.states

import com.marcossan.freshfy.data.model.Product

/**
 * Son los elementos que forman parte de los estados y a su vez, cuándo estos elementos reciban
 * modificaciones por parte del usuario, se redibujará la IU.
 */
data class ProductState(
    val product: Product  = initializeProduct()
)

// TODO: Eliminar
private fun initializeProduct(): Product {
    // Lógica para inicializar el producto, puedes proporcionar valores predeterminados o cargarlos desde algún lugar
    return Product(
        4,
        barcode = "8424818268292",
        name = "Soja",
        imageUrl = "https://images.openfoodfacts.org/images/products/842/481/826/8292/front_es.9.400.jpg",
        expirationDate = 0L,
        expirationDateInString = "",
        dateAdded = "",
        quantity = "1"
    )
}