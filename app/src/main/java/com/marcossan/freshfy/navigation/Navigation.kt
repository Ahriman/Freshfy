package com.marcossan.freshfy.navigation

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.marcossan.freshfy.viewmodels.ProductViewModel
import com.marcossan.freshfy.views.AddProductScreen
import com.marcossan.freshfy.views.EditProductScreen
import com.marcossan.freshfy.views.ProductScreen
import com.marcossan.freshfy.views.ProductsListScreen

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Navigation(
    productViewModel: ProductViewModel
) {
    // Solicitar permisos al iniciar la aplicación
    val permissionState =
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

    LaunchedEffect(true) {
        permissionState.launchPermissionRequest()
    }

    permissionState.status.isGranted

//    if (permissionState.status.isGranted) {
//        Text(text = "El permiso fue condecido")
//    } else if (!permissionState.status.shouldShowRationale) {
//        Text(text = "Mostrar racional")
//    } else {
//        Text(text = "El permiso fue denegado")
//    }

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.ProductsListScreen.route) {
        composable(Screens.ProductsListScreen.route) {
            ProductsListScreen(navController, productViewModel)
        }

//        // Entrada por defecto
        composable(Screens.AddProductScreen.route) {
            AddProductScreen(
                navController,
                productViewModel,
                it.arguments?.getString("barcode") ?: ""
            )
        }

        // Entrada desde el escáner de códigos de barras
        composable("${Screens.AddProductScreen.route}/{barcode}", arguments = listOf(
            navArgument("barcode") { type = NavType.StringType }
        )) {
            AddProductScreen(
                navController, productViewModel, it.arguments?.getString("barcode") ?: "",
            )
        }

        composable(
            Screens.EdiProductScreen.route, arguments = listOf(
                navArgument("id") { type = NavType.LongType },
            )
        ) {
            EditProductScreen(
                navController,
                productViewModel,
                it.arguments!!.getLong("id"),
            )
        }

        composable(
            route = Screens.ProductScreen.route,
            arguments = listOf(
                navArgument("barcode") { type = NavType.StringType },
                navArgument("id") { type = NavType.LongType },
            )
        ) {
            val barcode = it.arguments?.getString("barcode")
            requireNotNull(barcode) { "No puede ser nulo porque la pantalla de producto necesita un código de barras" }
            ProductScreen(
                navController,
                productViewModel = productViewModel,
                barcode = barcode,
                it.arguments!!.getLong("id"),
            )
        }

        composable(Screens.Notification.route) {
            NotificationScreen(
                onBackPress = {
                    navController.popBackStack()
                }
            )
        }
    }


}

@Composable
fun NotificationScreen(onBackPress: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Contenido de tu pantalla de notificaciones
        Text("¡Tienes una nueva notificación!")

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para regresar a la pantalla principal
        Button(
            onClick = onBackPress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Atrás")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Volver a la pantalla principal")
        }
    }
}