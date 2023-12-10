package com.marcossan.despensa.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.marcossan.despensa.data.local.dao.ProductsDatabaseDao
import com.marcossan.despensa.data.model.Product

/**
 * La anotación @Database se utiliza para marcar una clase que actuará como la base de datos de la
 * aplicación. Esta clase debe ser abstracta y extender la clase RoomDatabase. Al usar @Database,
 * se está definiendo la estructura y configuración de la base de datos local.
 */
@Database(
    entities = [Product::class],
    version = 1,
    exportSchema = false
)
abstract class ProductsDatabase : RoomDatabase() {
    abstract fun productsDao(): ProductsDatabaseDao
}