package com.marcossan.freshfy.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.marcossan.freshfy.data.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Dao
interface ProductsDatabaseDao {
    @Query("SELECT * FROM products")
    fun getAll(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE code = :barcode")
    suspend fun getProduct(barcode: String): Product // getProductById

    @Query("SELECT * FROM products WHERE id = :id")
    fun getProductById(id: Int): Product

    @Query("SELECT * FROM products WHERE expirationDate >= :days")
    fun getProductsThatExpiredInDays(days: Long): Flow<List<Product>>

    @Insert
    suspend fun addProduct(product: Product) // Insert

//    @Update
//    fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)










    @Query("SELECT * FROM products WHERE id = :productId")
//    fun getProductById(productId: Long?): Flow<Product?>
    suspend fun getProductById(productId: Long): Product?

    @Update
    suspend fun updateProduct(product: Product)


}