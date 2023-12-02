package com.marcossan.despensa.states

import com.marcossan.despensa.models.Product

/**
 * Son los elementos que forman parte de los estados y a su vez, cuándo estos elementos reciban
 * modificaciones por parte del usuario, se redibujará la IU.
 */
data class ProductsState(
    val products: List<Product> = emptyList()
//    val products: List<Product> = list
)

val list = mutableListOf(
    Product(
        4,
        code = "8424818268292",
        name = "Soja",
        "https://images.openfoodfacts.org/images/products/842/481/826/8292/front_es.9.400.jpg",
    ),
    Product(
        5,
        code = "8480010090277",
        name = "Canela",
        "https://images.openfoodfacts.org/images/products/848/001/009/0277/front_es.6.400.jpg",
    ),
    Product(
        6,
        code = "8480000073297",
        name = "Soja texturizada",
        "https://images.openfoodfacts.org/images/products/848/000/007/3297/front_es.99.400.jpg",
    ),
    Product(
        45,
        code = "8431876291117",
        name = "Rulo tofu y algas",
        "https://images.openfoodfacts.org/images/products/843/187/629/1117/front_es.19.400.jpg",
    ),
    Product(
        66,
        code = "20659318",
        name = "Bebida de Avena",
        "https://images.openfoodfacts.org/images/products/20659318/front_es.77.400.jpg",
    ),
    Product(
        77,
        code = "8422767123167",
        name = "Aceite De Oliva Virgen Extra\"",
        "https://images.openfoodfacts.org/images/products/842/276/712/3167/front_es.29.400.jpg",
    ),
)