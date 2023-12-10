package com.marcossan.despensa.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.marcossan.despensa.data.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductsDatabaseDao {
    @Query("SELECT * FROM products")
    fun getAll(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id = :id")
    fun getProduct(id: Int): Flow<Product> // getProductById

    @Insert
    suspend fun addProduct(product: Product) // Insert

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

}