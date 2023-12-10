package com.marcossan.despensa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
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
                    val productViewModel by viewModels<ProductViewModel>()
                    Navigation(productViewModel = productViewModel)
                }
            }
        }
    }
}
