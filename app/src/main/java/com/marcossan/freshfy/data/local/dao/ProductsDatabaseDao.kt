package com.marcossan.freshfy.data.local.dao

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
    fun getAll(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id = :barcode")
    fun getProduct(barcode: String): Flow<Product> // getProductById

    @Query("SELECT * FROM products WHERE id = :barcode")
    fun getProduct2(barcode: String): Product // getProductById

    @Query("SELECT * FROM products WHERE expirationDate >= :days")
    fun getProductsThatExpiredInDays(days: Int): Flow<List<Product>>

    @Insert
    suspend fun addProduct(product: Product) // Insert

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

}