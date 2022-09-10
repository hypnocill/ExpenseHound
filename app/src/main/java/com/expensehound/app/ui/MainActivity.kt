package com.expensehound.app.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.expensehound.app.R
import com.expensehound.app.data.PurchaseItem
import com.expensehound.app.ui.nav.AppScreens
import com.expensehound.app.ui.nav.DemoAddNewPurchaseTopAppBar
import com.expensehound.app.ui.nav.DemoBottomNavigation
import com.expensehound.app.ui.nav.DemoNavHost
import com.expensehound.app.ui.nav.DemoTopAppBar
import com.expensehound.app.ui.theme.ComposeTemplateTheme
import kotlin.random.Random

@ExperimentalMaterialNavigationApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val demoViewModel = MainViewModel()
        setContent {
            PresApp(demoViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("RestrictedApi") // has to do with NavController - check after updating this lib
@ExperimentalMaterialNavigationApi
@Composable
fun PresApp(demoViewModel: MainViewModel) {
    ComposeTemplateTheme {

        val navScreens = listOf(
            AppScreens.HomeNav,
            AppScreens.Future,
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
                DemoBottomNavigation(navController = navController, items = navScreens)
            }, topBar = {
                val newPurchaseInput = demoViewModel.newPurchaseInput

                if (demoViewModel.newPurchaseIntent.value) {
                    DemoAddNewPurchaseTopAppBar(
                        onDismiss = {
                            demoViewModel.newPurchaseIntent.value = false
                        },
                        onSave = {
                            demoViewModel.newPurchaseIntent.value = false

                            if (newPurchaseInput.text.value != NewPurchaseItemInputInitialValues.text
                                && newPurchaseInput.price.value != NewPurchaseItemInputInitialValues.price
                            ) {
                                val newPurchaseItem =
                                    PurchaseItem(
                                        Random.nextInt(20, 1000),
                                        newPurchaseInput.text.value,
                                        newPurchaseInput.image.value,
                                        newPurchaseInput.image.value.toString(),
                                        newPurchaseInput.selectedCategory.value,
                                        newPurchaseInput.price.value.toDouble()
                                    )
                                demoViewModel.purchasesList.add(0, newPurchaseItem)

                                Toast.makeText(
                                    context,
                                    "Успешно добавихте " + newPurchaseInput.text.value,
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            resetNewPurchaseInput(newPurchaseInput)
                        }
                    )
                } else {
                    DemoTopAppBar(navController = navController)
                }
            }) { innerPadding ->
                DemoNavHost(
                    navController = navController,
                    demoViewModel,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

fun resetNewPurchaseInput(item: NewPurchaseItemInput) {
    item.text.value = NewPurchaseItemInputInitialValues.text
    item.price.value = NewPurchaseItemInputInitialValues.price
    item.selectedCategory.value = NewPurchaseItemInputInitialValues.selectedCategory
    item.image.value = NewPurchaseItemInputInitialValues.image
}

// move to a separate file
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppFab(navController: NavHostController, demoViewModel: MainViewModel) {
    val backstackEntry = navController.currentBackStackEntryAsState()
    var purchaseFabVisible = false
    var futurePurchaseFabVisible = false

    if (backstackEntry.value?.destination?.route == AppScreens.HomeNav.route) {
        purchaseFabVisible = true
    }
    if (backstackEntry.value?.destination?.route == AppScreens.Future.route) {
        futurePurchaseFabVisible = true
    }

    val transformOrigin = TransformOrigin(0.5f, 0.5f)

    var onExitAnimation = scaleOut(
        animationSpec = spring(dampingRatio = 1.5f),
        transformOrigin = transformOrigin,
    )

    if (demoViewModel.newPurchaseIntent.value || demoViewModel.newFuturePurchaseIntent.value) {
        purchaseFabVisible = false
        futurePurchaseFabVisible = false

        onExitAnimation = scaleOut(
            animationSpec = tween(
                durationMillis = 200, easing = CubicBezierEasing(0.42f, 0.0f, 0.58f, 1.0f)
            ), transformOrigin = TransformOrigin(0.95f, 0.95f), targetScale = 4f
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = 200, easing = CubicBezierEasing(0.42f, 0.0f, 0.58f, 1.0f)
            )
        )
    }

    //Extract to function that accepts 'visible' and 'onClick'
    AnimatedVisibility(
        visible = purchaseFabVisible,
        enter = scaleIn(
            animationSpec = spring(dampingRatio = 1.5f), transformOrigin = transformOrigin
        ),
        exit = onExitAnimation,
    ) {
        LargeFloatingActionButton(
            onClick = {
                demoViewModel.newPurchaseIntent.value = true
            },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_add_24),
                contentDescription = "FAB",
            )
        }
    }

    AnimatedVisibility(
        visible = futurePurchaseFabVisible,
        enter = scaleIn(
            animationSpec = spring(dampingRatio = 1.5f), transformOrigin = transformOrigin
        ),
        exit = onExitAnimation,
    ) {
        LargeFloatingActionButton(
            onClick = {
                Log.d("TAG", "opened bro")
            },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_add_24),
                contentDescription = "FAB",
            )
        }
    }
}

