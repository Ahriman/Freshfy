package com.marcossan.despensa

import android.app.Application
import androidx.room.Room
import com.marcossan.despensa.data.local.ProductRepository
import com.marcossan.despensa.data.local.dao.ProductsDatabaseDao
import com.marcossan.despensa.data.local.database.ProductsDatabase
import com.marcossan.despensa.viewmodels.ProductViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideProductsDao(application: Application): ProductsDatabaseDao {
        val database = Room.databaseBuilder(
                application,
                ProductsDatabase::class.java,
                "db_products")
                .build()
        val dao = database.productsDao()
        return dao
    }

    @Provides
    @Singleton
    fun provideProductViewModel(productRepository: ProductRepository): ProductViewModel {
        return ProductViewModel(productRepository = productRepository)
    }

    @Provides
    @Singleton
    fun provideRepository(dao: ProductsDatabaseDao): ProductRepository {
        return ProductRepository(productDao = dao)
    }


}