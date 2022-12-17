package com.expensehound.app.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import com.expensehound.app.R
import com.expensehound.app.ui.components.AppFab
import com.expensehound.app.ui.navigation.AddNewEntityTopAppBar
import com.expensehound.app.ui.navigation.AppScreens
import com.expensehound.app.ui.navigation.BottomNavigation
import com.expensehound.app.ui.navigation.DemoNavHost
import com.expensehound.app.ui.navigation.TopAppBar
import com.expensehound.app.ui.theme.ExpenseHoundTheme
import com.expensehound.app.ui.viewmodel.MainViewModel
import com.expensehound.app.ui.viewmodel.StatsViewModel
import com.expensehound.app.utils.onIncomeInputSave
import com.expensehound.app.utils.onPurchaseInputSave
import com.expensehound.app.utils.resetIncomeInput
import com.expensehound.app.utils.resetNewPurchaseInput
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator

//TODO: Refactor this file to make it short and lean
@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("RestrictedApi")
@ExperimentalMaterialNavigationApi
@Composable
fun App(viewModel: MainViewModel, statsViewModel: StatsViewModel) {
    ExpenseHoundTheme {

        val navScreens = listOf(
            AppScreens.HomeNav,
            AppScreens.Stats,
        )

        val context = LocalContext.current
        val navController = rememberNavController()
        val bottomSheetNavigator = rememberBottomSheetNavigator()
        navController.navigatorProvider += bottomSheetNavigator

        ModalBottomSheetLayout(bottomSheetNavigator) {
            Scaffold(floatingActionButton = {
                val backstackEntry = navController.currentBackStackEntryAsState()

                AppFab(viewModel, backstackEntry)

            }, bottomBar = {
                BottomNavigation(navController = navController, items = navScreens)
            }, topBar = {

                if (viewModel.newPurchaseIntent.value) {
                    val titleStringId =
                        if (viewModel.newPurchaseInput.id.value == null) R.string.new_purchase else R.string.new_purchase_edit

                    AddNewEntityTopAppBar(
                        title = stringResource(id = titleStringId),
                        onDismiss = {
                            viewModel.newPurchaseIntent.value = false
                            resetNewPurchaseInput(viewModel.newPurchaseInput)
                        },
                        onSave = {
                            onPurchaseInputSave(
                                viewModel.newPurchaseIntent,
                                viewModel.newPurchaseInput,
                                context,
                                viewModel,
                                true
                            )
                        }
                    )
                } else if (viewModel.newFuturePurchaseIntent.value) {
                    val titleStringId =
                        if (viewModel.newPurchaseInput.id.value == null) R.string.new_future_purchase else R.string.new_purchase_edit

                    AddNewEntityTopAppBar(
                        title = stringResource(id = titleStringId),
                        onDismiss = {
                            viewModel.newFuturePurchaseIntent.value = false
                            resetNewPurchaseInput(viewModel.newFuturePurchaseInput)
                        },
                        onSave = {
                            onPurchaseInputSave(
                                viewModel.newFuturePurchaseIntent,
                                viewModel.newFuturePurchaseInput,
                                context,
                                viewModel,
                                false
                            )
                        }
                    )
                } else if (viewModel.newIncomeIntent.value) {
                    val titleStringId =
                        if (viewModel.newIncomeInput.id.value == null) R.string.new_income else R.string.edit_income

                    AddNewEntityTopAppBar(
                        title = stringResource(id = titleStringId),
                        onDismiss = {
                            viewModel.newIncomeIntent.value = false
                            resetIncomeInput(viewModel.newIncomeInput)
                        },
                        onSave = {
                            onIncomeInputSave(
                                viewModel.newIncomeIntent,
                                viewModel.newIncomeInput,
                                context,
                                viewModel,
                            )
                        }
                    )
                } else {
                    TopAppBar(navController = navController)
                }
            }) { innerPadding ->
                DemoNavHost(
                    navController = navController,
                    viewModel,
                    modifier = Modifier.padding(innerPadding),
                    statsViewModel
                )
            }
        }
    }
}
