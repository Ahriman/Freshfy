package com.marcossan.freshfy.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad products de la base de datos local
 */
@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo("code")
    var barcode: String,
    @ColumnInfo("name")
    var name: String,
    @ColumnInfo("imageUrl")
    val imageUrl: String,
    @ColumnInfo("expirationDate")
    var expirationDate: Long,
    @ColumnInfo("expirationDateInString")
    val expirationDateInString: String,
    @ColumnInfo("dateAdded")
    val dateAdded: Long = 0,
    @ColumnInfo("dateAddedInString")
    val dateAddedInString: String = "",
    @ColumnInfo("quantity")
    var quantity: String = "1",
    @ColumnInfo("notificationId")
    var notificationId: Int? = null // ID de notificación asociado al producto
)