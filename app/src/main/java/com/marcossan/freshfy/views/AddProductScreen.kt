package com.marcossan.freshfy.views

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.marcossan.freshfy.R
import com.marcossan.freshfy.data.model.Product
import com.marcossan.freshfy.navigation.Screens
import com.marcossan.freshfy.utils.BarcodeScanner
import com.marcossan.freshfy.utils.ExpiryNotificationManager
import com.marcossan.freshfy.utils.ScannerResult
import com.marcossan.freshfy.utils.Utils
import com.marcossan.freshfy.viewmodels.ProductViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    navController: NavController,
    productViewModel: ProductViewModel,
    barcode: String?
) {

    lateinit var barcodeScanner: BarcodeScanner

    val context = LocalContext.current
    barcodeScanner = BarcodeScanner(context, navController)

    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }
    val scaffoldState = rememberScaffoldState()



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
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState, modifier = Modifier.fillMaxWidth()
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Bottom app bar",
                )
            }
        },

        ) {
        ContentAddProductScreen(
            it = it,
            navController = navController,
            productViewModel = productViewModel,
            onScanBarcode = {
//                barcodeScanner.startScan(productViewModel)
                val scannerResult: ScannerResult = barcodeScanner.startScan(productViewModel)
                // show snackbar as a suspend function
//                snackbarHostState.showSnackbar("Snackbar # " + stringResult)
                when (scannerResult) {
                    ScannerResult.OK -> {
                        snackbarHostState.showSnackbar("FAILED")
                    }

                    ScannerResult.CANCELED -> {
                        snackbarHostState.showSnackbar("CANCELED")
                    }

                    ScannerResult.FAILED -> {
                        snackbarHostState.showSnackbar("FAILED")
                    }

                    else -> {

                    }
                }
//                scope.launch {
//                    snackbarHostState.showSnackbar(message = stringResult.toString(), duration = SnackbarDuration.Long)
//                }
//                snackbarHostState.showSnackbar(message = stringResult.toString(), duration = SnackbarDuration.Long)
            },
            barcode = barcode ?: ""
        )
//            onScanBarcode = { barcodeScanner.startScan() })
    }
}

//@SuppressLint("StateFlowValueCalledInComposition", "CoroutineCreationDuringComposition")
@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentAddProductScreen(
    it: PaddingValues,
    navController: NavController,
    productViewModel: ProductViewModel,
    onScanBarcode: suspend () -> Unit,
    barcode: String
) {

    // Use LaunchedEffect to perform a side effect when the composable is (re)composed
    LaunchedEffect(true) {// Con true se ejecuta la primera vez que se compose la pantalla
        // Perform some asynchronous operation here, e.g., fetch data from a repository
        //            val data = fetchDataFromRepository()

        // Update the UI with the fetched data
        // This will only run once when the composable is initially composed or re-composed
        // due to the key parameter (true in this example)

        // Limpiar campos
        productViewModel.clearData()


    }

    val scope = rememberCoroutineScope()

    // Habilitar o deshabilitar el botón según si el campo de texto está vacío o no
    var isAddButtonEnabled by remember { mutableStateOf(false) }

//    var code by remember { mutableStateOf(code) } // TODO
//
//    productViewModel.getProduct(barcode = code)

    Column(
        modifier = Modifier
            .padding(it)
            .padding(top = 30.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = productViewModel.barcode,
//            value = code,
//            onValueChange = { code = it },
//            onValueChange = { productViewModel.onBarcodeChange(it) },
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
//            isError = !viewModel.isValidBarcode,
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
//            isError = !viewModel.isValidProductQuantity,
            singleLine = true,
        )


        // Actualizar el estado del botón
        // Deshabilitar hasta rellenar todos los campos obligatorios
        isAddButtonEnabled = productViewModel.barcode.isNotBlank()
                && productViewModel.productName.isNotBlank()
                && productViewModel.productExpireDate.isNotBlank()
//                && productViewModel.productQuantity.isNotBlank()


        // Usar este método para obtener un producto con los datos de la API - TODO: Quitar de aquí
        // TODO: Lo estoy llamando 2 veces..
        productViewModel.getProduct(barcode = barcode) // TODO:

        //TODO
        val product = Product(
            barcode = productViewModel.barcode,
            name = productViewModel.productName,
            imageUrl = productViewModel.productUrl,
            expirationDate = Utils.getTimeMillisOfStringDate(productViewModel.productExpireDate),
            expirationDateInString = productViewModel.productExpireDate,
            dateAdded = System.currentTimeMillis(),
            dateAddedInString = Utils.getStringDateFromMillis(System.currentTimeMillis()),
            quantity = productViewModel.productQuantity
        )

//        ProductQuantity(product = product)

        scope.launch {
            productViewModel.onProductChange(product = product)
        }



        var errorMessage: String
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
//                val product = productViewModel.getProduct(code) // TODO

                // TODO
                val product = Product(

                    barcode = productViewModel.barcode,
                    name = productViewModel.productName,
                    imageUrl = productViewModel.productUrl,
                    expirationDate = Utils.getTimeMillisOfStringDate(productViewModel.productExpireDate),
                    expirationDateInString = productViewModel.productExpireDate,
                    dateAdded = System.currentTimeMillis(),
                    dateAddedInString = Utils.getStringDateFromMillis(System.currentTimeMillis()),
                    quantity = productViewModel.productQuantity
                )

//                productViewModel.addProduct(productViewModel.product)
                // TODO
                try {
                    productViewModel.addProduct(product = product)
                } catch (e: Exception) {
                    // SnackBar
                    errorMessage = e.message.toString()
                }


                // MOVER
//                productViewModel.sendNotificacion(context = context)


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

        //TODO
        // Añadir notificación programada
//        productViewModel.checkAndScheduleNotification(LocalContext.current, product)
//        productViewModel.scheduleNotification(context, notificationId = 1, delayMillis = 5000)
//        val expiryNotificationManager = ExpiryNotificationManager(context = context)
//        expiryNotificationManager.scheduleExpiryNotification(Date(product.expirationDate))

        Button(
            onClick = {
                navController.navigate(Screens.Notification.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notificaciones")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Ver Notificaciones")
        }

    }
}





