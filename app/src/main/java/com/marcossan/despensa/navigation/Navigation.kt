package com.marcossan.despensa.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.marcossan.despensa.views.AddProductScreen
import com.marcossan.despensa.viewmodels.ProductViewModel
import com.marcossan.despensa.views.EditProductScreen
import com.marcossan.despensa.views.ProductsScreen
import com.marcossan.despensa.views.BarcodeScannerScreen

@Composable
fun Navigation(
    productViewModel: ProductViewModel
) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "start") {
        composable("start") {
            ProductsScreen(navController, productViewModel)
        }

//        // Entrada por defecto
        composable("add") {
            AddProductScreen(navController, productViewModel, it.arguments?.getString("code") ?: "")
        }

        // Entrada desde el escáner de códigos de barras
        composable("add/{code}", arguments = listOf(
            navArgument("code") { type = NavType.StringType }
        )) {
            AddProductScreen(
                navController, productViewModel, it.arguments?.getString("code") ?: "",
            )
        }

        composable("edit/{id}/{code}/{name}", arguments = listOf(
            navArgument("id") { type = NavType.IntType },
            navArgument("code") { type = NavType.StringType }, // TODO: Int
            navArgument("name") { type = NavType.StringType }
        )) {
            EditProductScreen(
                navController,
                productViewModel,
                it.arguments!!.getInt("id"),
                it.arguments?.getString("code"),
                it.arguments?.getString("name")
            )
        }

// java.lang.IllegalArgumentException: Navigation destination that matches request NavDeepLinkRequest{
// uri=android-app://androidx.navigation/scanner_screen/20195335 }
// cannot be found in the navigation graph ComposeNavGraph(0x0) startDestination={Destination(0xa2cd94f5) route=start}

        composable(
            route = Screens.ScannerApp.route,
            arguments = listOf(navArgument("barcode") { type = NavType.StringType })
        ) {
            val barcode = it.arguments?.getString("barcode")
            requireNotNull(barcode) { "No puede ser nulo porque la pantalla de producto necesita un código de barras" }
            BarcodeScannerScreen(
                navController,
                productViewModel = productViewModel,
                barcode = barcode
            )
        }
    }

}