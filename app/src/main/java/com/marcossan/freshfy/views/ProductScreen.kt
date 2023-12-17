@file:OptIn(ExperimentalMaterial3Api::class)

package com.marcossan.freshfy.views

import android.annotation.SuppressLint
import androidx.appcompat.widget.PopupMenu
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.marcossan.freshfy.R
import com.marcossan.freshfy.data.model.Product
import com.marcossan.freshfy.viewmodels.ProductViewModel
import com.marcossan.freshfy.viewmodels.ScannerUiState


@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    navController: NavController,
    productViewModel: ProductViewModel,
    barcode: String?,
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

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(
                        text = stringResource(R.string.product_screen_title),
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    ShowMenuOptions(product = product, productViewModel = productViewModel)
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
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            productViewModel.getProductFromApi(barcode = barcode)
            ContentProductScreen(
                navController = navController,
                productViewModel = productViewModel,
                scannerUiState = productViewModel.scannerUiState,
                product = product
            )
        }
    }
}

@Composable
fun ContentProductScreen(
    navController: NavController,
    productViewModel: ProductViewModel,
    scannerUiState: ScannerUiState,
    modifier: Modifier = Modifier,
    product: Product?
) {

    ResultScreen(
        productViewModel,
        scannerUiState = scannerUiState,
        product = product,
        modifier = modifier.fillMaxWidth()
    )

}


/**
 * ResultScreen displaying number of photos retrieved.
 */
@Composable
fun ResultScreen(
    productViewModel: ProductViewModel,
    product: Product?,
    modifier: Modifier = Modifier,
    scannerUiState: ScannerUiState
) {
    val context = LocalContext.current

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = product?.name ?: "", textAlign = TextAlign.Center, fontSize = 18.sp)
        Spacer(modifier = Modifier.padding(16.dp))
        Column {
            Text(text = stringResource(R.string.barcode_result, product?.barcode ?: ""))

            // TODO Extraer Strings
            Text(text = stringResource(R.string.date_product_expire) + ": ${product?.expirationDateInString}")
            Text(text = stringResource(R.string.product_quantity) + ": ${product?.quantity}")
            Text(text = stringResource(R.string.product_added_day) + ": ${product?.dateAddedInString}")
        }
        Spacer(modifier = Modifier.padding(16.dp))

        when (scannerUiState) {
            is ScannerUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
            is ScannerUiState.Success -> AsyncImage(
                model = product?.imageUrl ?: "",
                contentDescription = "",
                modifier = Modifier.size(450.dp)
            )

            is ScannerUiState.Error -> ErrorScreen()
        }

//        Button(
//            onClick = {
//                if (product != null) {
//                    productViewModel.sendNotificacion(context = context, product = product)
//                }
//            }
//        ) {
//            Text(text = stringResource(R.string.test_notification_button))
//        }

    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun ShowMenuOptions(product: Product?, productViewModel: ProductViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val density = LocalDensity.current.density
    val anchorPosition = DpOffset(x = 4.dp * density, y = 0.dp)

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(R.string.test_notification_button),
            tint = Color.White
        )
    }

    val context = LocalContext.current

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.background(Color.White),
        offset = anchorPosition
    ) {
        DropdownMenuItem(onClick = { /* Handle option 1 click */ }, text = {
            Text(stringResource(R.string.test_notification_button), modifier = Modifier.clickable {
                if (product != null) {
                    productViewModel.sendNotificacion(context = context, product = product)
                }
            })
        })
    }
}