package com.marcossan.freshfy.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.marcossan.freshfy.utils.Utils
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Entidad products de la base de datos local
 */
@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("code")
    var code: String,
    @ColumnInfo("name")
    var name: String,
    @ColumnInfo("imageUrl")
    val imageUrl: String,
    @ColumnInfo("expirationDate")
    val expirationDate: Long,
    @ColumnInfo("expirationDateInString")
    val expirationDateInString: String,
    @ColumnInfo("dateAdded")
    val dateAdded: String = "",
    @ColumnInfo("quantity")
    val quantity: String = "1"
)

