package com.marcossan.despensa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.room.Room
import com.marcossan.despensa.navigation.Navigation
import com.marcossan.despensa.data.local.ProductRepository
import com.marcossan.despensa.data.local.database.ProductsDatabase
import com.marcossan.despensa.ui.theme.DespensaTheme
import com.marcossan.despensa.viewmodels.ProductViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DespensaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val database =
                        Room.databaseBuilder(this, ProductsDatabase::class.java, "db_products")
                            .build()
                    val dao = database.productsDao()
                    val productRepository = ProductRepository(productDao = dao)

//                    val productViewModel = ProductViewModel(dao = dao)
                    val productViewModel = ProductViewModel(productRepository = productRepository)
                    Navigation(productViewModel = productViewModel)
                }
            }
        }
    }
}
