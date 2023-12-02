package com.marcossan.despensa.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.marcossan.despensa.R
import com.marcossan.despensa.models.Product
import com.marcossan.despensa.viewmodels.ProductsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsListView(
    navController: NavController,
    viewModel: ProductsViewModel
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
        ContentListProductView(it, navController, viewModel)
    }
}

@Composable
fun ContentListProductView(
    it: PaddingValues,
    navController: NavController,
    viewModel: ProductsViewModel
) {
    val state = viewModel.state

    Column(modifier = Modifier.padding(it)) {
        LazyColumn {
            items(state.products) { product ->

                ProductListItem(
                    navController = navController,
                    viewModel = viewModel,
                    product = product,
                    onOpenProductItem = {
                        navController.navigate(route = "scanner/${product.code}")
                    }
                )

            }
        }
    }
}

@Composable
fun ProductListItem(
    navController: NavController,
    viewModel: ProductsViewModel,
    product: Product,
    onOpenProductItem: () -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenProductItem() }
    ) {
//        AsyncImage(
//            model = product.imageUrl,
//            contentDescription = "",
//            modifier = Modifier
//                .padding(top = 15.dp)
//                .size(80.dp)
//        )
        Column(modifier = Modifier.padding(16.dp)) {
            // Nombre
            Text(
                text = product.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
            // Marca
            Text(
                text = product.code,
            )
//            // Fecha caducidad
//            Text(
//                text = product.expirationDate,
//            )
//            // Fecha añadido
//            Text(
//                text = product.dateAdded,
//            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.CenterVertically),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = {
                navController.navigate("edit/${product.id}/${product.code}/${product.name}")
            }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = stringResource(R.string.edit))
            }
            IconButton(onClick = {
                viewModel.deleteProduct(product)
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