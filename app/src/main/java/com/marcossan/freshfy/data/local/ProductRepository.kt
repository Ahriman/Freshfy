package com.marcossan.freshfy.data.local

import androidx.lifecycle.LiveData
import com.marcossan.freshfy.data.local.dao.ProductsDatabaseDao
import com.marcossan.freshfy.data.model.Product
import kotlinx.coroutines.flow.Flow

class ProductRepository(
    private val productDao: ProductsDatabaseDao
) {

    fun getAllProductsAsState(): LiveData<List<Product>> {
        return productDao.getAllProductsAsState()
    }

    suspend fun addProduct(product: Product) {
        productDao.addProduct(product)
    }

    suspend fun getAll(): Flow<List<Product>> {
        return productDao.getAll()
    }

    suspend fun getProduct(barcode: String): Product {
        return productDao.getProduct(barcode)
    }

    suspend fun getProductsThatExpiredInDays(days: Long): Flow<List<Product>> {
        return productDao.getProductsThatExpiredInDays(days)
    }

    suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }

    fun getProductById(productId: Long): LiveData<Product> {
        return productDao.getProductById(productId)
    }

    suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product)
    }
}
