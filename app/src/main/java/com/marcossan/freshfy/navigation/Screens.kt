package com.marcossan.freshfy.navigation

sealed class Screens(val route: String){
    data object ProductsListScreen: Screens("main_screen")
    data object AddProductScreen: Screens("add")
    data object EdiProductScreen: Screens("edit/{id}/{barcode}")
    data object ProductScreen: Screens("scanner/{barcode}/{id}")
    data object Notification : Screens("notification")

}
