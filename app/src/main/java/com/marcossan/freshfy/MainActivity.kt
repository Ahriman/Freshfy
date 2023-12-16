package com.marcossan.freshfy

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.marcossan.freshfy.navigation.Navigation
import com.marcossan.freshfy.ui.theme.DespensaTheme
import com.marcossan.freshfy.viewmodels.EditProductViewModel
import com.marcossan.freshfy.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
                    val editProductViewModel by viewModels<EditProductViewModel>()
                    Navigation(productViewModel = productViewModel, editProductViewModel = editProductViewModel)

                }
            }
        }
    }
}
