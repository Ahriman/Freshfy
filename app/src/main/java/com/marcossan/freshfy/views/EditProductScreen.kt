package com.marcossan.freshfy.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.marcossan.freshfy.R
import com.marcossan.freshfy.data.model.Product
import com.marcossan.freshfy.utils.BarcodeScanner
import com.marcossan.freshfy.viewmodels.ProductViewModel
import com.marcossan.freshfy.views.elements.ProductQuantity
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

    val scope = rememberCoroutineScope()

    // Habilitar o deshabilitar el botón según si el campo de texto está vacío o no
    var isAddButtonEnabled by remember { mutableStateOf(false) }

//    scope.launch {
//        editProductViewModel.getProduct(barcode = product?.barcode ?: "")
//    }
//
//    val newProduct by rememberUpdatedState(newValue = editProductViewModel.product)


//    var barcode by remember { mutableStateOf(product?.barcode) } // TODO
//    var name by remember { mutableStateOf(product?.name) } // TODO
//    var expirationDate by remember { mutableStateOf(Utils.getStringDateFromMillis(product?.expirationDate ?: 0L)) } // TODO
//    var quantity by remember { mutableStateOf(product?.quantity) } // TODO

//    var product by remember { mutableStateOf(product) } // TODO


    // Obtener datos producto
//    productViewModel.getProduct(barcode ?: "")


//    println("Código: $barcode")
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
            value = product?.barcode ?: "",
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
//                product?.barcode = productBarcode
                product = product?.copy(barcode = productBarcode)
                // En cada cambio del input, buscar el nombre del producto y añadirlo al campo nombre
                if (productBarcode.isNotEmpty()) {
                    scope.launch {
                        productViewModel.getProductFromApi(productBarcode)
                        if (productViewModel.productName.isNotBlank()) {
                            product = product?.copy(name = productViewModel.productName)
                            product = product?.copy(imageUrl = productViewModel.productUrl)
                        }
                        productViewModel.clearData()
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
            value = product?.name ?: "",
            onValueChange = { productName ->
//                product?.name = productName
                product = product?.copy(name = productName)
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
                    product = product?.copy(expirationDate = date ?: 0L)
                    product = product?.copy(expirationDateInString = dateExpired)

                }


//                date.let {
//                    val localDate =
//                        Instant.ofEpochMilli(it ?: 0).atZone(ZoneId.of("UTC"))
//                            .toLocalDate()
//                    dateExpired =
//                        "${localDate.dayOfMonth}/${localDate.monthValue}/${localDate.year}"
////                    productViewModel.onProductExpireDateChange(dateExpired)
//                    productViewModel.onProductExpireDateChange(dateExpired)
////                    product.?expirationDate = dateExpired
//                    product?.expirationDate = date ?: 0L
//                }
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
            value = product?.expirationDateInString ?: "",
            onValueChange = { productExpireDate ->
//                product.expirationDate = productExpireDate.toLong()
                product = product?.copy(expirationDate = productExpireDate.toLong())
            },
//            value = productViewModel.productExpireDate,
//            onValueChange = { productExpireDate ->
//                productViewModel.onProductExpireDateChange(
//                    productExpireDate
//                )
//            },
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
            //TODO Impedir que se pegue texto
//            value = productViewModel.productQuantity.replaceFirstChar { it.uppercase() },
//            value = productViewModel.productQuantity,
//            onValueChange = { productQuantity ->
//                productViewModel.onProductQuantityChange(
//                    productQuantity
//                )
//            },
            value = product?.quantity ?: "1",
            onValueChange = { productQuantity ->
//                product?.quantity = productQuantity
                if (productQuantity.isNotBlank() && productQuantity.length <= 3 && productQuantity.toInt() in 1..999) {
                    product = product?.copy(quantity = productQuantity)
                }
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

//        var newProduct = product
//
//        product?.let { product ->
//            ProductQuantity(
//                product = product, onProductQuantityChange = {
//                    newProduct = product.copy(quantity = product.quantity)
//                },
//                scope = scope
//            )
//        } // TODO: El componente aún no está listo
//
//        product = newProduct


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
//                val product = Product(id = id, code = code!!, name = name!!, imageUrl = "") // TODO imageUrl

//                val product = Product(
//                    id = product.id,
//                    barcode = product.barcode,
//                    name = product.name,
//                    imageUrl = product.imageUrl, // TODO
//                    expirationDate = Utils.getTimeMillisOfStringDate(product.expirationDateInString),
//                    expirationDateInString = product.expirationDateInString,
//                    dateAdded = "lala", // TODO
//                    quantity = product.quantity
//                )

//                if (code != null) {
//                    suspend { productViewModel.updateProduct(productViewModel.getProductByBarcode2(code)) }
//                }

                if (product != null) {
                    productViewModel.updateProduct(product = product!!) // TODO
                }
//                productViewModel.updateProduct(product ?: Product(0,"", "", "", 0L, "", "", ""))
                navController.popBackStack()
            },
            enabled = isAddButtonEnabled
        ) {
            Text(text = stringResource(R.string.edit))
        }

    }
}

