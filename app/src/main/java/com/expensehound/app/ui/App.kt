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
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import com.expensehound.app.R
import com.expensehound.app.ui.components.AppFab
import com.expensehound.app.ui.nav.AddNewEntityTopAppBar
import com.expensehound.app.ui.nav.AppScreens
import com.expensehound.app.ui.nav.BottomNavigation
import com.expensehound.app.ui.nav.DemoNavHost
import com.expensehound.app.ui.nav.DemoTopAppBar
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

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("RestrictedApi")
@ExperimentalMaterialNavigationApi
@Composable
fun App(demoViewModel: MainViewModel, statsViewModel: StatsViewModel) {
    ExpenseHoundTheme {

        val navScreens = listOf(
            AppScreens.HomeNav,
            AppScreens.Future,
            AppScreens.Income,
            AppScreens.Stats,
        )

        val context = LocalContext.current
        val navController = rememberNavController()
        val bottomSheetNavigator = rememberBottomSheetNavigator()
        navController.navigatorProvider += bottomSheetNavigator

        ModalBottomSheetLayout(bottomSheetNavigator) {
            Scaffold(floatingActionButton = {
                AppFab(navController, demoViewModel)
            }, bottomBar = {
                BottomNavigation(navController = navController, items = navScreens)
            }, topBar = {

                if (demoViewModel.newPurchaseIntent.value) {
                    val titleStringId =
                        if (demoViewModel.newPurchaseInput.id.value == null) R.string.new_purchase else R.string.new_purchase_edit

                    AddNewEntityTopAppBar(
                        title = stringResource(id = titleStringId),
                        onDismiss = {
                            demoViewModel.newPurchaseIntent.value = false
                            resetNewPurchaseInput(demoViewModel.newPurchaseInput)
                        },
                        onSave = {
                            onPurchaseInputSave(
                                demoViewModel.newPurchaseIntent,
                                demoViewModel.newPurchaseInput,
                                context,
                                demoViewModel,
                                true
                            )
                        }
                    )
                } else if (demoViewModel.newFuturePurchaseIntent.value) {
                    val titleStringId =
                        if (demoViewModel.newPurchaseInput.id.value == null) R.string.new_future_purchase else R.string.new_purchase_edit

                    AddNewEntityTopAppBar(
                        title = stringResource(id = titleStringId),
                        onDismiss = {
                            demoViewModel.newFuturePurchaseIntent.value = false
                            resetNewPurchaseInput(demoViewModel.newFuturePurchaseInput)
                        },
                        onSave = {
                            onPurchaseInputSave(
                                demoViewModel.newFuturePurchaseIntent,
                                demoViewModel.newFuturePurchaseInput,
                                context,
                                demoViewModel,
                                false
                            )
                        }
                    )
                } else if (demoViewModel.newIncomeIntent.value) {
                    val titleStringId =
                        if (demoViewModel.newIncomeInput.id.value == null) R.string.new_income else R.string.edit_income

                    AddNewEntityTopAppBar(
                        title = stringResource(id = titleStringId),
                        onDismiss = {
                            demoViewModel.newIncomeIntent.value = false
                            resetIncomeInput(demoViewModel.newIncomeInput)
                        },
                        onSave = {
                            onIncomeInputSave(
                                demoViewModel.newIncomeIntent,
                                demoViewModel.newIncomeInput,
                                context,
                                demoViewModel,
                            )
                        }
                    )
                } else {
                    DemoTopAppBar(navController = navController)
                }
            }) { innerPadding ->
                DemoNavHost(
                    navController = navController,
                    demoViewModel,
                    modifier = Modifier.padding(innerPadding),
                    statsViewModel
                )
            }
        }
    }
}
