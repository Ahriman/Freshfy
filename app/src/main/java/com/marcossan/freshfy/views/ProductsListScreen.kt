package com.marcossan.freshfy.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.marcossan.freshfy.R
import com.marcossan.freshfy.data.model.Product
import com.marcossan.freshfy.viewmodels.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsListScreen(
    navController: NavController,
    viewModel: ProductViewModel
) {



    // Utiliza un MutableState para observar los cambios en la lista de productos
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }

    // Observa los cambios en la lista de productos
    DisposableEffect(Unit) {
        val observer = Observer<List<Product>> { productList ->
            products = productList
        }

        viewModel.allProducts.observeForever(observer)

        onDispose {
            // Desvincula la observación cuando el componente se dispara
            viewModel.allProducts.removeObserver(observer)
        }
    }




    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add") }, // TODO: Crear rutas de Screens
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add),
                    tint = Color.White
                )
            }
        }
    ) {
        if (viewModel.state.products.isNotEmpty()) {
            ContentProductsListScreen(it, navController, viewModel, products)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 80.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.products_screen_list_empty_message),
                    modifier = Modifier.padding(horizontal = 15.dp),
                    textAlign = TextAlign.Justify,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
            }
        }

    }
}

@Composable
fun ContentProductsListScreen(
    it: PaddingValues,
    navController: NavController,
    viewModel: ProductViewModel,
    products: List<Product>
) {
//    val state = viewModel.state

    Column(modifier = Modifier.padding(it)) {
        LazyColumn {
//            items(state.products) { product ->
            items(products) { product ->

                ProductListItem(
                    navController = navController,
                    viewModel = viewModel,
                    product = product,
                    onOpenProductItem = { navController.navigate(route = "scanner/${product.barcode}/${product.id}") } // TODO
                )

            }
        }
    }
}

@Composable
fun ProductListItem(
    navController: NavController,
    viewModel: ProductViewModel,
    product: Product,
    onOpenProductItem: () -> Unit,
) {


    var isDeleteDialogVisible by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenProductItem() }
    ) {
        Box(
            modifier = Modifier
                .weight(2f)
                .align(Alignment.CenterVertically)
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = stringResource(R.string.image_content_description),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(80.dp),
                alignment = Alignment.Center
            )
        }
        Column(
            modifier = Modifier
                .weight(5f)
                .padding(vertical = 12.dp)
        ) {
            // Nombre
            Text(
                text = product.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
            // TODO: Marca
            Text(
                text = stringResource(R.string.code, product.barcode),
            )
            // Fecha caducidad
            Text(
                text = stringResource(R.string.expire, product.expirationDateInString),
            )
            // Fecha añadido
            Text(
                text = stringResource(R.string.added, product.dateAddedInString),
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 8.dp)
                .align(alignment = Alignment.CenterVertically),
            horizontalAlignment = Alignment.End
        ) {
            IconButton(onClick = {
                navController.navigate("edit/${product.id}/${product.barcode}") // /${product.name}/${product.expirationDate}/${product.quantity}
            }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit)
                )
            }
            IconButton(onClick = {

                isDeleteDialogVisible = true
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete),
                    tint = Color.Red
                )
            }

            // Mostrar diálogo de confirmación de eliminación
            if (isDeleteDialogVisible) {
                DeleteProductDialog(
                    onConfirm = {
                        viewModel.deleteProduct(product)
                        isDeleteDialogVisible = false
                    },
                    onDismiss = {
                        isDeleteDialogVisible = false
                    }
                )
            }
        }

    }

    Divider()

}


@Composable
fun DeleteProductDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onDismiss.invoke()
        },
        title = {
            Text("Eliminar producto")
        },
        text = {
            Text("¿Estás seguro de que deseas eliminar este producto?")
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm.invoke()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss.invoke()
                }
            ) {
                Text("Cancelar")
            }
        },
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
        },
        iconContentColor = Color.Red
    )
}