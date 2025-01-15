package com.jodifrkh.firebase.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jodifrkh.firebase.ui.view.DetailMhsView
import com.jodifrkh.firebase.ui.view.HomeScreen
import com.jodifrkh.firebase.ui.view.InsertMhsView

@Composable
fun MainControllerPage(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = DestinasiHome.route
    ) {
        composable(
            route = DestinasiHome.route
        ) {
            HomeScreen(
                onDetailClick = { nim ->
                    navController.navigate("${DestinasiDetail.route}/$nim")
                },
                navigateToItemEntry = {
                    navController.navigate(DestinasiInsert.route)
                },
            )
        }

        composable(DestinasiInsert.route) {
            InsertMhsView(
                onBack = { navController.popBackStack() },
                OnNavigate = {
                    navController.navigate(DestinasiHome.route)
                }
            )
        }

        composable(
            DestinasiDetail.routesWithArg,
            arguments = listOf(
                navArgument(DestinasiDetail.nim) {
                    type = NavType.StringType
                }
            )
        ) {
            val nim = it.arguments?.getString(DestinasiDetail.nim)
            nim?.let { nim ->
                DetailMhsView(
                    onBack = {
                        navController.popBackStack()
                    }

                )
            }
        }
    }
}