package com.marcossan.freshfy.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.marcossan.freshfy.data.model.Product
import com.marcossan.freshfy.viewmodels.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    navController: NavController,
    viewModel: ProductViewModel,
    id: Int,
    code: String?,
    name: String?
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Editar View",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack, contentDescription = "Atrás",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) {
        ContentEditProductScreen(
            it = it,
            navController = navController,
            viewModel = viewModel,
            id = id, code = code,
            name = name
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentEditProductScreen(
    it: PaddingValues,
    navController: NavController,
    viewModel: ProductViewModel,
    id: Int,
    code: String?,
    name: String?
) {
    var code by remember { mutableStateOf(code) }
    var name by remember { mutableStateOf(name) }

    Column(
        modifier = Modifier
            .padding(it)
            .padding(top = 30.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = code ?: "",
            onValueChange = { code = it },
            label = { Text(text = "Código de barras") },
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .padding(bottom = 15.dp)

        )

        OutlinedTextField(
            value = name ?: "",
            onValueChange = { name = it },
            label = { Text(text = "Nombre") },
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .padding(bottom = 15.dp)

        )

        Button(
            onClick = {
                val product = Product(id = id, code = code!!, name = name!!, imageUrl = "") // TODO imageUrl

                viewModel.updateProduct(product)
                navController.popBackStack()
            }
        ) {
            Text(text = "Editar")
        }
    }
}
