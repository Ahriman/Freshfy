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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
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
import com.marcossan.freshfy.viewmodels.EditProductViewModel
import com.marcossan.freshfy.viewmodels.ProductViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    navController: NavController,
    productViewModel: ProductViewModel,
    editProductViewModel: EditProductViewModel,
    id: Int,
    barcode: String?,
//    name: String?,
//    expirationDate: Long?,
//    quantity: String?,
) {


    lateinit var barcodeScanner: BarcodeScanner

    val context = LocalContext.current
    barcodeScanner = BarcodeScanner(context, navController)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.edit_screen_title),
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
        ContentEditProductScreen(
            it = it,
            navController = navController,
            productViewModel = productViewModel,
            editProductViewModel = editProductViewModel,
            onScanBarcode = { barcodeScanner.startScan(productViewModel) },
            id = id,
            barcode = barcode,
//            name = name,
//            expirationDate = expirationDate,
//            quantity = quantity,
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentEditProductScreen(
    it: PaddingValues,
    navController: NavController,
    productViewModel: ProductViewModel,
    editProductViewModel: EditProductViewModel,
    onScanBarcode: suspend () -> Unit,
    id: Int,
    barcode: String?,
//    name: String?,
//    expirationDate: Long?,
//    quantity: String?,
) {
    val scope = rememberCoroutineScope()

    scope.launch {
        editProductViewModel.getProduct(barcode = barcode ?: "")
    }

    val newProduct by rememberUpdatedState(newValue = editProductViewModel.product)



    var barcode by remember { mutableStateOf(newProduct.code) } // TODO
    var name by remember { mutableStateOf(newProduct.name) } // TODO
    var expirationDate by remember { mutableStateOf(Utils.getStringDateFromMillis(newProduct.expirationDate)) } // TODO
    var quantity by remember { mutableStateOf(newProduct.quantity) } // TODO


    // Obtener datos producto
//    productViewModel.getProduct(barcode ?: "")



    println("Código: $barcode")
//    if (code != null) {
//        LaunchedEffect(code) {
//            productViewModel.getProduct(code)
//        }
//    }


//    if (code != null) {
//        productViewModel.getProduct(code)
//        scope.launch { productViewModel.getProductByBarcode(code) }
//
//    }

    // Trigger the search when the barcode changes
//    LaunchedEffect(code) {
//        val product = code?.let { code -> productViewModel.getProductByBarcode(code) }
//        if (product != null) {
//            productViewModel.onProductChange(product)
//        }
//    }


    println("Producto de base de datos: ${productViewModel.product}")

    Column(
        modifier = Modifier
            .padding(it)
            .padding(top = 30.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
//            value = if (newProduct.code.isNullOrBlank()) {
//                productViewModel.barcode
//            } else {
//                newProduct.code
//            },
            value = barcode,
//            value = code,
//            onValueChange = { code = it },
//            onValueChange = { productViewModel.onBarcodeChange(it) },
//            onValueChange = { productBarcode ->
//                if (newProduct.code.isBlank()) {
//                    productViewModel.onBarcodeChange(productBarcode)
//                } else {
//                    newProduct.code = productBarcode
//                }
//            },
            onValueChange = { productBarcode ->
                barcode = productBarcode
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
//            isError = !viewModel.isValidBarcode,
            singleLine = true

        )

        OutlinedTextField(
//            value = productViewModel.productName,
//            onValueChange = { productName ->
//                productViewModel.onProductNameChange(productName)
//            },
            value = name,
            onValueChange = { productName ->
                name = productName
            },
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .padding(bottom = 15.dp),
            label = { Text(text = stringResource(R.string.product_name)) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
//            isError = !viewModel.isValidProductName,
            singleLine = true

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
                        Text(text = stringResource(R.string.confirm))
                    }
                },
//                modifier = Modifier.padding(15.dp),
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
//                    productViewModel.onProductExpireDateChange(dateExpired)
                    productViewModel.onProductExpireDateChange(dateExpired)
                    expirationDate = dateExpired
                }
                DatePicker(state = state)
            }

        }

        OutlinedTextField(
//            value = Utils.getStringDateFromMillis(expirationDate ?: 0L),
//            onValueChange = { expirationDate = Utils.getTimeMillisOfStringDate(it) },
//            value = productViewModel.productExpireDate,
//            onValueChange = { productExpireDate ->
//                productViewModel.onProductExpireDateChange(
//                    productExpireDate
//                )
//            },
            value = expirationDate,
            onValueChange = { productExpireDate ->
                expirationDate = productExpireDate
            },
//            value = productViewModel.productExpireDate,
//            onValueChange = { productExpireDate ->
//                productViewModel.onProductExpireDateChange(
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
            //TODO Impedir que se pegue texto
//            value = productViewModel.productQuantity.replaceFirstChar { it.uppercase() },
//            value = productViewModel.productQuantity,
//            onValueChange = { productQuantity ->
//                productViewModel.onProductQuantityChange(
//                    productQuantity
//                )
//            },
            value = quantity,
            onValueChange = { productQuantity ->
                quantity = productQuantity
            },
//            modifier = Modifier.weight(0.8f),
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .padding(bottom = 15.dp),
            label = { Text(text = stringResource(R.string.product_quantity)) },
            placeholder = { Text(text = "1") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
//            isError = !viewModel.isValidProductQuantity,
            singleLine = true,
        )

        // Botón para guardar los cambios

        Button(
            onClick = {
//                val product = Product(id = id, code = code!!, name = name!!, imageUrl = "") // TODO imageUrl
                val product = Product(
                    id = id,
                    code = barcode,
                    name = name,
                    imageUrl = productViewModel.productUrl, // TODO
                    expirationDate = Utils.getTimeMillisOfStringDate(expirationDate),
                    expirationDateInString = expirationDate,
                    dateAdded = "lala", // TODO

                    quantity = quantity
                )

//                if (code != null) {
//                    suspend { productViewModel.updateProduct(productViewModel.getProductByBarcode2(code)) }
//                }

                productViewModel.updateProduct(product)
                navController.popBackStack()
            }
        ) {
            Text(text = stringResource(R.string.edit))
        }

    }
}
