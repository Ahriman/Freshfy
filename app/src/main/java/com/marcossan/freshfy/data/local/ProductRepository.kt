package com.marcossan.freshfy.data.local

import com.marcossan.freshfy.data.local.dao.ProductsDatabaseDao
import com.marcossan.freshfy.data.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

//TODO ELIMINAR
class ProductRepository(
    private val productDao: ProductsDatabaseDao
) {

//    suspend fun insertProduct(product: Product) {
//        productDao.insert(product)
//    }
    suspend fun addProduct(product: Product) {
        productDao.addProduct(product)
    }

    suspend fun getAll(): Flow<List<Product>> {
        return productDao.getAll()
    }

    suspend fun getProduct(barcode: String): Product {
        return productDao.getProduct(barcode)
    }

    suspend fun getProduct2(barcode: String): Product {
        return productDao.getProduct(barcode)
    }

    suspend fun getProductsThatExpiredInDays(days: Long): Flow<List<Product>> {
        return productDao.getProductsThatExpiredInDays(days)
    }

    suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }


    suspend fun getProduct(productId: Long): Product? {
        return productDao.getProductById(productId)
    }

    suspend fun getProductById(productId: Long): Product? {
        return productDao.getProductById(productId)
    }

    suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product)
    }
}
