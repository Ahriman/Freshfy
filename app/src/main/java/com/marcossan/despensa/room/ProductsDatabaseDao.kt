package com.marcossan.despensa.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.marcossan.despensa.models.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductsDatabaseDao {
    @Query("SELECT * FROM products")
    fun getAll(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id = :id")
    fun getProduct(id: Int): Flow<Product>

    @Insert
    suspend fun addProduct(product: Product)

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

}