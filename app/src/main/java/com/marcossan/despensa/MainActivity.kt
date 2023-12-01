package com.marcossan.despensa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.marcossan.despensa.navigation.Navigation
import com.marcossan.despensa.room.ProductsDatabase
import com.marcossan.despensa.ui.theme.DespensaTheme
import com.marcossan.despensa.viewmodels.ProductsViewModel

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
                    val database = Room.databaseBuilder(this, ProductsDatabase::class.java, "db_products").build()
                    val dao = database.productsDao()

                    val productViewModel = ProductsViewModel(dao = dao)
                    Navigation(productViewModel = productViewModel)
                }
            }
        }
    }
}
