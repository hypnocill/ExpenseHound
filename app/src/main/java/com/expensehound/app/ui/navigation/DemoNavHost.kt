package com.expensehound.app.ui.nav

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.expensehound.app.ui.screens.desires.DesiresScreen
import com.expensehound.app.ui.screens.income.IncomeScreen
import com.expensehound.app.ui.screens.purchase_details.PurchaseDetailsScreen
import com.expensehound.app.ui.screens.purchases.PurchasesScreen
import com.expensehound.app.ui.screens.stats.StatsScreen
import com.expensehound.app.ui.viewmodel.MainViewModel
import com.expensehound.app.ui.viewmodel.StatsViewModel
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet

@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalMaterialNavigationApi
@Composable
fun DemoNavHost(
    navController: NavHostController,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    statsViewModel: StatsViewModel
) {
    NavHost(
        navController = navController,
        startDestination = AppScreens.HomeNav.route,
        modifier = modifier
    ) {
        composable(route = AppScreens.HomeNav.route) {
            PurchasesScreen(viewModel) { item, index ->
                navController.navigate("${AppScreens.Detail.route}/${item.uid}")
            }
        }
        composable(route = AppScreens.Future.route) {
            viewModel.newPurchaseIntent.value = false
            DesiresScreen(viewModel) { item, index ->
                navController.navigate("${AppScreens.FutureDetail.route}/${item.uid}")
            }
        }
        composable(route = AppScreens.Income.route) {
            viewModel.newIncomeIntent.value = false
            IncomeScreen(viewModel) { item, index ->
                // TODO: ADD NAVIGATION
            }
        }
        composable(route = AppScreens.Stats.route) {
            StatsScreen(viewModel, statsViewModel)
        }
        bottomSheet(
            route = "${AppScreens.Detail.route}/{uid}",
            arguments = listOf(
                navArgument("uid") {
                    type = NavType.IntType
                }
            )
        ) { entry ->
            val index = entry.arguments?.getInt("uid", -1)
            PurchaseDetailsScreen(viewModel, index!!)
        }
        bottomSheet(
            route = "${AppScreens.FutureDetail.route}/{uid}",
            arguments = listOf(
                navArgument("uid") {
                    type = NavType.IntType
                }
            ),
        ) { entry ->
            val index = entry.arguments?.getInt("uid", -1)
            PurchaseDetailsScreen(viewModel, index!!)
        }
    }
}


