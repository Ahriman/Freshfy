package com.marcossan.freshfy.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.marcossan.freshfy.R
import com.marcossan.freshfy.data.model.Product
import com.marcossan.freshfy.utils.BarcodeScanner
import com.marcossan.freshfy.viewmodels.ProductViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    navController: NavController,
    productViewModel: ProductViewModel,
    productId: Long
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
            onScanBarcode = { barcodeScanner.startScan(productViewModel) },
            productId = productId
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
    onScanBarcode: suspend () -> Unit,
    productId: Long
) {

    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    // Utiliza un MutableState para observar los cambios en el producto
    var product by remember { mutableStateOf<Product?>(null) }

    // Observa los cambios en el producto específico
    DisposableEffect(Unit) {
        val observer = Observer<Product?> { updatedProduct ->
            updatedProduct?.let {
                product = updatedProduct
            }
        }

        // Utiliza un método en tu ViewModel para obtener el producto específico por ID
        productViewModel.getProductById(productId = productId).observeForever(observer)

        onDispose {
            // Desvincula la observación cuando el componente se dispara
            productViewModel.getProductById(productId = productId).removeObserver(observer)
        }
    }

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
            value = if (productViewModel.isBarcodeScanned) {
                productViewModel.barcode
            } else {
                product?.barcode ?: ""
            },
            onValueChange = { productBarcode ->
                if (productViewModel.isBarcodeScanned) {
                    productViewModel.onBarcodeChange(productBarcode)
                    product = product?.copy(barcode = productViewModel.barcode)
                    productViewModel.setIsBarcodeScanned(false)
                } else {
                    product = product?.copy(barcode = productBarcode)
                }

                // En cada cambio del input, buscar el nombre del producto y añadirlo al campo nombre
                if (productBarcode.isNotEmpty()) {

                }

                scope.launch {
                    if (productBarcode.isNotEmpty()) {
                        productViewModel.getProductFromApi(productBarcode)
                    }

                    if (productViewModel.productName.isNotBlank()) {

                        product = product?.copy(name = productViewModel.productName)
                        product = product?.copy(imageUrl = productViewModel.productUrl)
                    }
                    productViewModel.clearData()

                }

            },
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .padding(bottom = 15.dp)
                .onFocusChanged {

                    if (productViewModel.isBarcodeScanned && productViewModel.barcode.isNotEmpty()) {
                        scope.launch {
                            productViewModel.getProductFromApi(productViewModel.barcode)
                            if (productViewModel.productName.isNotBlank()) {

                                product = product?.copy(name = productViewModel.productName)
                                product = product?.copy(imageUrl = productViewModel.productUrl)
                            }
                        }
                    }
                }
                .focusRequester(focusRequester),
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
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            singleLine = true

        )

        OutlinedTextField(
            value = product?.name ?: "",
            onValueChange = { productName ->
                product = product?.copy(name = productName)
            },
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .padding(bottom = 15.dp),
            label = { Text(text = stringResource(R.string.product_name)) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            singleLine = true

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
//                    productViewModel.onProductExpireDateChange(dateExpired)
                    product = product?.copy(expirationDate = date ?: 0L)
                    product = product?.copy(expirationDateInString = dateExpired)

                }
                DatePicker(state = state)
            }

        }

        OutlinedTextField(
            value = product?.expirationDateInString ?: "",
            onValueChange = { productExpireDate ->
//                product.expirationDate = productExpireDate.toLong()
                product = product?.copy(expirationDate = productExpireDate.toLong())
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
//                isError = !viewModel.validarFormatoFecha(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
        )

        OutlinedTextField(
            value = product?.quantity ?: "1",
            onValueChange = { productQuantity ->
                if (productQuantity.isNotBlank() && productQuantity.length <= 3 && productQuantity.toInt() in 1..999) {
                    product = product?.copy(quantity = productQuantity)
                }
            },
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .padding(bottom = 15.dp),
            label = { Text(text = stringResource(R.string.product_quantity)) },
            placeholder = { Text(text = "1") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.clearFocus() }),
            singleLine = true,
        )

        // Actualizar el estado del botón
        // Deshabilitar hasta rellenar todos los campos obligatorios
        // TODO: Mejorar comprobación si los datos son válidos
        isAddButtonEnabled = product?.barcode?.isNotBlank() ?: false
                && product?.name?.isNotBlank() ?: false
                && product?.expirationDateInString?.isNotBlank() ?: false
//                && product?.quantity?.isNotBlank() ?: false

        // Botón para guardar los cambios
        Button(
            onClick = {
                if (product != null) {
                    productViewModel.updateProduct(product = product!!) // TODO
                }
                navController.popBackStack()
            },
            enabled = isAddButtonEnabled
        ) {
            Text(text = stringResource(R.string.edit))
        }

    }
}

