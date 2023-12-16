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
import com.google.accompanist.permissions.shouldShowRationale
import com.marcossan.freshfy.viewmodels.EditProductViewModel
import com.marcossan.freshfy.views.AddProductScreen
import com.marcossan.freshfy.viewmodels.ProductViewModel
import com.marcossan.freshfy.views.EditProductScreen
import com.marcossan.freshfy.views.ProductsListScreen
import com.marcossan.freshfy.views.BarcodeScannerScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Navigation(
    productViewModel: ProductViewModel,
    editProductViewModel: EditProductViewModel
) {
    // Solicitar permisos al iniciar la aplicación
    NotificationScreen()

//    LaunchedEffect(key1 = true) {
////        productViewModel.requestPermission()
//
//
//    }


    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "start") {
        composable("start") {
            ProductsListScreen(navController, productViewModel)
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

        composable(Screens.EdiProductScreen.route, arguments = listOf(
            navArgument("id") { type = NavType.LongType },
//            navArgument("barcode") { type = NavType.StringType }, // TODO: Long?
//            navArgument("name") { type = NavType.StringType },
//            navArgument("expirationDate") { type = NavType.LongType },
//            navArgument("quantity") { type = NavType.StringType }, // TODO: Int?
        )) {
            EditProductScreen(
                navController,
                productViewModel,
                it.arguments!!.getLong("id"),
//                it.arguments?.getString("barcode"),
//                it.arguments?.getString("name"),
////                it.arguments?.getString("expirationDate"),
//                it.arguments?.getLong("expirationDate"),
//                it.arguments?.getString("quantity"),
            )
        }

// java.lang.IllegalArgumentException: Navigation destination that matches request NavDeepLinkRequest{
// uri=android-app://androidx.navigation/scanner_screen/20195335 }
// cannot be found in the navigation graph ComposeNavGraph(0x0) startDestination={Destination(0xa2cd94f5) route=start}

        composable(
            route = Screens.BarcodeScannerScreen.route,
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


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationScreen() {
    val permissionState =
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

    LaunchedEffect(true) {
        permissionState.launchPermissionRequest()
    }

    permissionState.status.isGranted

    if (permissionState.status.isGranted) {
        androidx.compose.material.Text(text = "El permiso fue condecido")
    } else if(!permissionState.status.shouldShowRationale){
        androidx.compose.material.Text(text = "Mostrar racional")
    } else {
        androidx.compose.material.Text(text = "El permiso fue denegado")
    }


    androidx.compose.material.Text(text = "Notificación")
}