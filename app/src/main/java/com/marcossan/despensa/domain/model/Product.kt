package com.marcossan.despensa.domain.model

//import com.marcossan.scanner.data.database.entities.ProductEntity

data class Product(
    val code: String,
    val name: String
)

fun Product.toDomain() = Product(code, name)
//fun ProductEntity.toDomain() = Product(code, name)