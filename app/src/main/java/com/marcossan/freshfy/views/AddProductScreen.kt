package com.marcossan.freshfy.views

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
import androidx.compose.runtime.LaunchedEffect
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
import com.marcossan.freshfy.R
import com.marcossan.freshfy.data.model.Product
import com.marcossan.freshfy.utils.BarcodeScanner
import com.marcossan.freshfy.utils.Utils
import com.marcossan.freshfy.viewmodels.ProductViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    navController: NavController,
    productViewModel: ProductViewModel
) {

    lateinit var barcodeScanner: BarcodeScanner

    val context = LocalContext.current
    barcodeScanner = BarcodeScanner(context, navController)

    Scaffold(

        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.add_product),
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
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back),
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
            productViewModel = productViewModel,
            onScanBarcode = {
                barcodeScanner.startScan(productViewModel)
            }
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentAddProductScreen(
    it: PaddingValues,
    navController: NavController,
    productViewModel: ProductViewModel,
    onScanBarcode: suspend () -> Unit
) {

    // Use LaunchedEffect to perform a side effect when the composable is (re)composed
    LaunchedEffect(true) {// Con true se ejecuta la primera vez que se compose la pantalla
        productViewModel.clearData()
    }

    val scope = rememberCoroutineScope()

    // Habilitar o deshabilitar el botón según si el campo de texto está vacío o no
    var isAddButtonEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(it)
            .padding(top = 30.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = productViewModel.barcode,
            onValueChange = { productBarcode ->
                productViewModel.onBarcodeChange(productBarcode)
                // En cada cambio del input, buscar el nombre del producto y añadirlo al campo nombre
                if (productBarcode.isNotEmpty()) {
                    scope.launch {
                        productViewModel.getProductFromApi(productBarcode)
                    }
                }
            },
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
            singleLine = true

        )

        OutlinedTextField(
            value = productViewModel.productName,
            onValueChange = { productName ->
                productViewModel.onProductNameChange(productName)
            },
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .padding(bottom = 15.dp),
            label = { Text(text = stringResource(R.string.product_name)) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
            singleLine = true,

            )

        val state = rememberDatePickerState()
        var showDialog by remember {
            mutableStateOf(false)
        }
        if (showDialog) {

            DatePickerDialog(
                onDismissRequest = {
                    showDialog = false
                },
                confirmButton = {
                    Button(onClick = { showDialog = false }) {
                        Text(text = stringResource(R.string.confirm))
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showDialog = false }) {
                        Text(text = stringResource(R.string.cancel))
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
                    productViewModel.onProductExpireDateChange(dateExpired)
                }
                DatePicker(state = state)
            }

        }

        OutlinedTextField(
            value = productViewModel.productExpireDate,
            onValueChange = { productExpireDate ->
                productViewModel.onProductExpireDateChange(
                    productExpireDate
                )
            },
            readOnly = true,
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
                        .clickable {
                            showDialog = true
                        }
                )
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
            singleLine = true,
        )

        OutlinedTextField(
            value = productViewModel.productQuantity,
            onValueChange = { productQuantity ->
                productViewModel.onProductQuantityChange(productQuantity)
            },
//            modifier = Modifier.weight(0.8f),
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .padding(bottom = 15.dp),
            label = { Text(text = stringResource(R.string.product_quantity)) },
            //placeholder = { Text(text = "1") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
        )

        // Actualizar el estado del botón
        // Deshabilitar hasta rellenar todos los campos obligatorios
        isAddButtonEnabled = productViewModel.barcode.isNotBlank()
                && productViewModel.productName.isNotBlank()
                && productViewModel.productExpireDate.isNotBlank()
//                && productViewModel.productQuantity.isNotBlank()


        //TODO
        val product = Product(
            barcode = productViewModel.barcode,
            name = productViewModel.productName,
            imageUrl = productViewModel.productUrl,
            expirationDate = Utils.getTimeMillisOfStringDate(productViewModel.productExpireDate),
            expirationDateInString = productViewModel.productExpireDate,
            dateAdded = System.currentTimeMillis(),
            dateAddedInString = Utils.getStringDateFromMillis(System.currentTimeMillis()),
            quantity = productViewModel.productQuantity.ifEmpty { "1" }
        )

        scope.launch {
            productViewModel.onProductChange(product = product)
        }

        Button(
            onClick = {
                productViewModel.addProduct(product = product)
                navController.popBackStack()
            },
            enabled = isAddButtonEnabled
        ) {
            Text(text = stringResource(id = R.string.add))
        }

        // La primera vez que entramos a la pantalla, no intenta cargar los datos
        // La segunda vez que se entra, es después de escanear el código y ya carga correctamente los datos.
        if (productViewModel.isBarcodeScanned) {
            scope.launch {// TODO anotacion
                productViewModel.getProductFromApi(productViewModel.barcode)
                productViewModel.setIsBarcodeScanned(false)
            }
        }

    }
}





