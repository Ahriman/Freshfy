package com.marcossan.freshfy.navigation

sealed class Screens(val route: String){
//    data object MainScreen: Screens("main_screen")
//    data object AddProductScreen: Screens("scanner_screen/{barcode}")
//    data object ScannerApp: Screens("scanner/{barcode}")
//    data object ProductScreen: Screens("product_screen")
    data object MainScreen: Screens("main_screen")
    data object AddProductScreen: Screens("scanner_screen/{barcode}")
    data object EdiProductScreen: Screens("edit/{id}/{barcode}/{name}/{expirationDate}/{quantity}")
    data object BarcodeScannerScreen: Screens("scanner/{barcode}")
    data object ProductScreen: Screens("product_screen")

    object Notification : Screens("notification")

}
