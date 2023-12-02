package com.marcossan.despensa.views

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.marcossan.despensa.BarcodeScanner
import com.marcossan.despensa.R
import com.marcossan.despensa.models.Product
import com.marcossan.despensa.viewmodels.ProductsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddView(navController: NavController, viewModel: ProductsViewModel) {

    lateinit var barcodeScanner: BarcodeScanner

    val context = LocalContext.current
    barcodeScanner = BarcodeScanner(context, navController)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Agregar producto",
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
        ContentAddProductView(
            it = it,
            navController = navController,
            viewModel = viewModel,
            onScanBarcode = { barcodeScanner.startScan(viewModel) })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentAddProductView(
    it: PaddingValues,
    navController: NavController,
    viewModel: ProductsViewModel,
    onScanBarcode: suspend () -> Unit,
) {

    val scope = rememberCoroutineScope()

    var code by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
//    var imageUrl by remember { mutableStateOf("") }
//    var expirationDate by remember { mutableStateOf("") }
//    var dateAdded by remember { mutableStateOf("") }
//    var quantity by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(it)
            .padding(top = 30.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = code,
            onValueChange = { code = it },
            label = { Text(text = stringResource(R.string.product_barcode)) },
            trailingIcon = {
                Icon(painter = painterResource(id = R.drawable.barcode_scanner),
                    contentDescription = stringResource(R.string.open_barcode_scanner_description),
                    modifier = Modifier.clickable {
                        scope.launch {
                            onScanBarcode()
                        }
                    })
            },
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .padding(bottom = 15.dp)

        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Nombre") },
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .padding(bottom = 15.dp)

        )

        Button(
            onClick = {
                val product = Product(
                    code = code,
                    name = name,
//                    imageUrl = imageUrl,
//                    expirationDate = expirationDate,
//                    dateAdded = dateAdded,
//                    quantity = quantity,
                )


                viewModel.addProduct(product)
                navController.popBackStack()
            }
        ) {
            Text(text = "Agregar")
        }
    }
}
