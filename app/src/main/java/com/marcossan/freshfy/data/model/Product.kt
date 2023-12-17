package com.marcossan.freshfy.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.marcossan.freshfy.utils.ExpiryNotificationManager
import com.marcossan.freshfy.viewmodels.ProductViewModel

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
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(barcode)
        parcel.writeString(name)
        parcel.writeString(imageUrl)
        parcel.writeLong(expirationDate)
        parcel.writeString(expirationDateInString)
        parcel.writeLong(dateAdded)
        parcel.writeString(dateAddedInString)
        parcel.writeString(quantity)
        parcel.writeValue(notificationId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}