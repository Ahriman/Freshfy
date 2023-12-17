package com.marcossan.freshfy.navigation

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
                productViewModel
            )
        }

        // Entrada desde el escáner de códigos de barras
        composable("${Screens.AddProductScreen.route}/{barcode}", arguments = listOf(
            navArgument("barcode") { type = NavType.StringType }
        )) {
            AddProductScreen(
                navController,
                productViewModel
            )
        }

        composable(
            "${Screens.EdiProductScreen.route}/{id}/{barcode}", arguments = listOf(
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
            route = "${Screens.ProductScreen.route}/{barcode}/{id}",
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

    }


}