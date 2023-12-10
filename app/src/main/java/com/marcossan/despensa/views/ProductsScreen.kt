package com.marcossan.despensa.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.marcossan.despensa.R
import com.marcossan.despensa.data.model.Product
import com.marcossan.despensa.viewmodels.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    navController: NavController,
    viewModel: ProductViewModel
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Despensa", color = Color.White, fontWeight = FontWeight.Bold)
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
            ContentProductsScreen(it, navController, viewModel)
            println("Test1")
        } else {
            println("Test2")
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 80.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "La lista de productos está vacía. \n\nAñade algún producto para comenzar con el control de fechas de caducidad.",
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
fun ContentProductsScreen(
    it: PaddingValues,
    navController: NavController,
    viewModel: ProductViewModel
) {
    val state = viewModel.state

    Column(modifier = Modifier.padding(it)) {
        LazyColumn {
            items(state.products) { product ->

                ProductListItem(
                    navController = navController,
                    viewModel = viewModel,
                    product = product,
                    onOpenProductItem = { navController.navigate(route = "scanner/${product.code}") }
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenProductItem() }
    ) {
        Box(modifier = Modifier.align(Alignment.CenterVertically)) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = "Imagen del producto",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(80.dp),
                alignment = Alignment.Center
            )
        }
        Column(modifier = Modifier.padding(16.dp)) {
            // Nombre
            Text(
                text = product.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
            // TODO: Marca
            Text(
                text = "Código: ${product.code}",
            )
            // Fecha caducidad
            Text(
                text = "Caduca: ${product.expirationDate}",
            )
            // Fecha añadido
            Text(
                text = "Añadido: ${product.dateAdded}",
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.CenterVertically),
            horizontalAlignment = Alignment.End
        ) {
            IconButton(onClick = {
                navController.navigate("edit/${product.id}/${product.code}/${product.name}")
            }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit)
                )
            }
            IconButton(onClick = {
//                viewModel.deleteProduct(product)
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete)
                )
            }
        }

    }

    Divider()

}