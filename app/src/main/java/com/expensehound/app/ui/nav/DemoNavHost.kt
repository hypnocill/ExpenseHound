package com.expensehound.app.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import com.expensehound.app.data.Category
import com.expensehound.app.data.PurchaseItem
import com.expensehound.app.ui.MainViewModel
import com.expensehound.app.ui.all_expenses.PurchasesScreen
import com.expensehound.app.ui.expense_details.DetailBody
import com.expensehound.app.ui.future_expenses.FutureExpensesScreen
import com.expensehound.app.ui.stats.StatsScreen
import kotlin.random.Random

@ExperimentalMaterialNavigationApi
@Composable
fun DemoNavHost(
    navController: NavHostController,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = AppScreens.HomeNav.route,
        modifier = modifier
    ) {
        composable(route = AppScreens.HomeNav.route) {
            PurchasesScreen(
                onItemClicked = { item, index ->
                    navController.navigate("${AppScreens.Detail.route}${index}")
                },
                viewModel
            )
        }
        composable(route = AppScreens.Future.route) {
            viewModel.newPurchaseIntent.value = false
            FutureExpensesScreen(
                onItemClicked = { item, index -> //navController.navigate("${AppScreens.Detail.route}${index}")
                },
                viewModel
            )
        }
        composable(route = AppScreens.Stats.route) {
            viewModel.newPurchaseIntent.value = false
            StatsScreen(
                onItemClicked = { item, index -> //navController.navigate("${AppScreens.Detail.route}${index}")
                },
                viewModel
            )
        }
        //  adb shell am start -a android.intent.action.VIEW -d "http://www.m3demo.com/details/red"
        bottomSheet(
            route = "${AppScreens.Detail.route}{purchaseListIndex}",
            arguments = listOf(
                navArgument("purchaseListIndex") {
                    type = NavType.IntType
                }
            ),
//            deepLinks = listOf(
//                navDeepLink {
//                    uriPattern = "{name}"
//                    uriPattern = "https://www.m3demo.com/details/{name}"
//                }
//            )
        ) { entry ->
            // Send ColorItem, or default if one is not found with that name
            val purchaseListItemIndex = entry.arguments?.getInt("purchaseListIndex", -1)
            val notFound = PurchaseItem(
                Random.nextInt(1000,1200),
                "Item Not Found",
                null,
                null,
                Category.OTHERS,
                0.0
            )
            val purchaseItem = viewModel.purchasesList[purchaseListItemIndex!!] ?: notFound
            DetailBody(
                purchaseItem = purchaseItem
            )
        }
    }
}
