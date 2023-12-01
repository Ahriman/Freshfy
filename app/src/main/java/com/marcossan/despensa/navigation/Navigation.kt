package com.marcossan.despensa.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.marcossan.despensa.views.AddView
import com.marcossan.despensa.viewmodels.ProductsViewModel
import com.marcossan.despensa.views.EditView
import com.marcossan.despensa.views.ProductsListView

@Composable
fun Navigation(
    productViewModel: ProductsViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "start") {
        composable("start") {
            ProductsListView(navController, productViewModel)
        }
        composable("add") {
            AddView(navController, productViewModel)
        }
        composable("edit/{id}/{code}/{name}", arguments = listOf(
            navArgument("id") { type = NavType.IntType },
            navArgument("code") { type = NavType.StringType }, // TODO: Int
            navArgument("name") { type = NavType.StringType }
        )) {
            EditView(
                navController,
                productViewModel,
                it.arguments!!.getInt("id"),
                it.arguments?.getString("code"),
                it.arguments?.getString("name")
            )
        }
    }

}