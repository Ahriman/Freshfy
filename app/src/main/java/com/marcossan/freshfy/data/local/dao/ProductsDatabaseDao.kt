package com.marcossan.freshfy.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.marcossan.freshfy.data.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductsDatabaseDao {

    @Query("SELECT * FROM products")
    fun getAllProductsAsState(): LiveData<List<Product>>

    @Query("SELECT * FROM products WHERE id = :productId")
    fun getProductById(productId: Long): LiveData<Product>

    @Query("SELECT * FROM products")
    fun getAll(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE code = :barcode")
    suspend fun getProduct(barcode: String): Product

    @Query("SELECT * FROM products WHERE id = :id")
    fun getProductById(id: Int): LiveData<Product>

    @Query("SELECT * FROM products WHERE expirationDate >= :days")
    fun getProductsThatExpiredInDays(days: Long): Flow<List<Product>>

    @Insert
    suspend fun addProduct(product: Product)

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

}