package com.marcossan.despensa.views

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.marcossan.despensa.utils.BarcodeScanner
import com.marcossan.despensa.R
import com.marcossan.despensa.viewmodels.ProductViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    navController: NavController,
    viewModel: ProductViewModel
) {

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
        ContentAddProductScreen(
            it = it,
            navController = navController,
            viewModel = viewModel,
            onScanBarcode = { barcodeScanner.startScan(viewModel) })
    }
}

@SuppressLint("StateFlowValueCalledInComposition", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentAddProductScreen(
    it: PaddingValues,
    navController: NavController,
    viewModel: ProductViewModel,
    onScanBarcode: suspend () -> Unit,
) {

    val scope = rememberCoroutineScope()

    var code by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var expirationDate by remember { mutableStateOf("") }
    var dateAdded by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(it)
            .padding(top = 30.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
//            value = viewModel.barcode,
            value = code,
            onValueChange = { code = it },
//            onValueChange = { viewModel.onBarcodeChange(it) },
//            onValueChange = { productBarcode ->
//                viewModel.onBarcodeChange(productBarcode)
//            },
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .padding(bottom = 15.dp),
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
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
//            isError = !viewModel.isValidBarcode,
            singleLine = true

        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .padding(bottom = 15.dp),
            label = { Text(text = stringResource(R.string.product_name)) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
//            isError = !viewModel.isValidProductName,
            singleLine = true,

            )

        val state = rememberDatePickerState()
        var showDialog by remember {
            mutableStateOf(false)
        }

//                    Button(onClick = { showDialog = true }) {
//                        Text(text = "Mostrar fecha")
//                    }
        if (showDialog) {

            DatePickerDialog(
                onDismissRequest = {
                    showDialog = false
                },
                confirmButton = {
                    Button(onClick = { showDialog = false }) {
                        Text(text = "Confirmar")
                    }
                },
//                modifier = Modifier.padding(15.dp),
                dismissButton = {
                    OutlinedButton(onClick = { showDialog = false }) {
                        Text(text = "Cancelar")
                    }
                }
            ) {
                // TODO:
                val dateExpired: String
                val date = state.selectedDateMillis
                date.let {
                    val localDate =
                        Instant.ofEpochMilli(it ?: 0).atZone(ZoneId.of("UTC"))
                            .toLocalDate()
                    dateExpired =
                        "${localDate.dayOfMonth}/${localDate.monthValue}/${localDate.year}"
                    viewModel.onProductExpireDateChange(dateExpired)
                }
                DatePicker(state = state)
            }

        }

        OutlinedTextField(
//            value = viewModel.productExpireDate,
            value = expirationDate,
            onValueChange = { expirationDate = it },
//            onValueChange = { productExpireDate ->
//                viewModel.onProductExpireDateChange(
//                    productExpireDate
//                )
//            },
//                            readOnly = true,
//                modifier = Modifier.weight(0.8f),
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .padding(bottom = 15.dp),
            label = { Text(text = stringResource(R.string.product_expire_data)) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
//                        .padding(4.dp)
                        .clickable {
                            showDialog = true
                        }
                )
            },
//                isError = !viewModel.validarFormatoFecha(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
            singleLine = true,
        )

        OutlinedTextField(
//                        value = productViewModel.productQuantity.replaceFirstChar { it.uppercase() },
//            value = viewModel.productQuantity,
            value = quantity,
//            onValueChange = { productQuantity ->
//                viewModel.onProductQuantityChange(
//                    productQuantity
//                )
//            },
            onValueChange = { quantity = it },
//            modifier = Modifier.weight(0.8f),
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .padding(bottom = 15.dp),
            label = { Text(text = stringResource(R.string.product_quantity)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
//            isError = !viewModel.isValidProductQuantity,
            singleLine = true,
        )

        // Usar este método para obtener un producto con los datos de la API - TODO: Quitar de aquí
        // TODO: Lo estoy llamando 2 veces..
        viewModel.getProduct(barcode = code) // TODO:

        Button(
            onClick = {
//                val product = Product(
//                    code = viewModel.product.code,
//                    name = viewModel.product.name,
//                    imageUrl = viewModel.product.imageUrl,
//                    expirationDate = expirationDate,
//                    dateAdded = dateAdded,
//                    quantity = viewModel.product.quantity,
//                )
                val product = viewModel.getProduct(code)
                println("product = ${product}")

                viewModel.addProduct(viewModel.product)
                navController.popBackStack()
            }
        ) {
            Text(text = "Agregar")
        }

        if (viewModel.isBarcodeScanned) {
            scope.launch {
                viewModel.getProductFromApi(viewModel.barcode)
                viewModel.setIsBarcodeScanned(false)
            }
        }

    }
}