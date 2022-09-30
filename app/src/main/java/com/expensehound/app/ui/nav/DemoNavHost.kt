package com.expensehound.app.ui.nav

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.expensehound.app.data.Category
import com.expensehound.app.data.PurchaseItem
import com.expensehound.app.data.StatsPurchaseItemsByCategory
import com.expensehound.app.ui.MainViewModel
import com.expensehound.app.ui.StatsViewModel
import com.expensehound.app.ui.screens.all_expenses.PurchasesScreen
import com.expensehound.app.ui.screens.expense_details.DetailBody
import com.expensehound.app.ui.screens.future_expenses.FutureExpensesScreen
import com.expensehound.app.ui.screens.stats.StatsScreen
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import kotlin.random.Random

val notFound = PurchaseItem(
    Random.nextInt(1000, 1200),
    "Item Not Found",
    null,
    Category.OTHERS,
    0.0,
    true
)

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
            PurchasesScreen(
                onItemClicked = { item, index ->
                    navController.navigate("${AppScreens.Detail.route}/${index}")
                },
                viewModel
            )
        }
        composable(route = AppScreens.Future.route) {
            viewModel.newPurchaseIntent.value = false
            FutureExpensesScreen(
                onItemClicked = { item, index ->
                    navController.navigate("${AppScreens.FutureDetail.route}/${index}")
                },
                viewModel
            )
        }
        composable(route = AppScreens.Stats.route) {
            var purchasesByCategoryList = remember {
                mutableStateListOf<StatsPurchaseItemsByCategory>()
            }

            LaunchedEffect(key1 = true) {
                viewModel.newPurchaseIntent.value = false

                statsViewModel.getAllPurchaseItemsGroupedByCategory(purchasesByCategoryList)
            }

            Log.d("asdas", purchasesByCategoryList.toList().toString())
            if (purchasesByCategoryList.isNotEmpty()) {
                StatsScreen(
                    purchasesByCategoryList
                )
            }
        }
        //  adb shell am start -a android.intent.action.VIEW -d "expensehoundapp://details/red"
        bottomSheet(
            route = "${AppScreens.Detail.route}/{purchaseListIndex}",
            arguments = listOf(
                navArgument("purchaseListIndex") {
                    type = NavType.IntType
                }
            )
        ) { entry ->
            val index = entry.arguments?.getInt("purchaseListIndex", -1)

            val purchaseItem = viewModel.purchasesList[index!!]
            DetailBody(
                purchaseItem = purchaseItem
            )
        }
        bottomSheet(
            route = "${AppScreens.FutureDetail.route}/{purchaseListIndex}",
            arguments = listOf(
                navArgument("purchaseListIndex") {
                    type = NavType.IntType
                }
            ),
        ) { entry ->
            val index = entry.arguments?.getInt("purchaseListIndex", -1)

            val purchaseItem = viewModel.futurePurchasesList[index!!]
            DetailBody(
                purchaseItem = purchaseItem
            )
        }
    }
}
