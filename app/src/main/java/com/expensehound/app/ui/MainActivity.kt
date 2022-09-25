package com.expensehound.app.ui

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.app.AlarmManagerCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import com.expensehound.app.R
import com.expensehound.app.data.PurchaseItem
import com.expensehound.app.ui.notifications.AlarmReceiver
import com.expensehound.app.ui.nav.AppScreens
import com.expensehound.app.ui.nav.DemoAddNewPurchaseTopAppBar
import com.expensehound.app.ui.nav.DemoBottomNavigation
import com.expensehound.app.ui.nav.DemoNavHost
import com.expensehound.app.ui.nav.DemoTopAppBar
import com.expensehound.app.ui.notifications.AppNotificationManager
import com.expensehound.app.ui.theme.ComposeTemplateTheme
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import java.util.*
import kotlin.random.Random

@ExperimentalMaterialNavigationApi
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val demoViewModel = MainViewModel()

        setContent {
            App(demoViewModel)
        }

        AppNotificationManager.createNotificationChannel(this)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("RestrictedApi") // has to do with NavController - check after updating this lib
@ExperimentalMaterialNavigationApi
@Composable
fun App(demoViewModel: MainViewModel) {
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

                if (demoViewModel.newPurchaseIntent.value) {
                    DemoAddNewPurchaseTopAppBar(
                        title = stringResource(id = R.string.new_purchase),
                        onDismiss = {
                            demoViewModel.newPurchaseIntent.value = false
                        },
                        onSave = {
                            onPurchaseInputSave(
                                demoViewModel.newPurchaseIntent,
                                demoViewModel.purchasesList,
                                demoViewModel.newPurchaseInput,
                                context,
                            )
                        }
                    )
                } else if (demoViewModel.newFuturePurchaseIntent.value) {
                    DemoAddNewPurchaseTopAppBar(
                        title = stringResource(id = R.string.new_future_purchase),
                        onDismiss = {
                            demoViewModel.newFuturePurchaseIntent.value = false
                        },
                        onSave = {
                            onPurchaseInputSave(
                                demoViewModel.newFuturePurchaseIntent,
                                demoViewModel.futurePurchasesList,
                                demoViewModel.newFuturePurchaseInput,
                                context,
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
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

fun onPurchaseInputSave(
    purchaseIntent: MutableState<Boolean>,
    purchasesList: SnapshotStateList<PurchaseItem>,
    input: BasePurchaseItemInput,
    context: Context,
) {
    purchaseIntent.value = false

    if (input.text.value != PurchaseItemInputInitialValues.text
        && input.price.value != PurchaseItemInputInitialValues.price
    ) {
        val newPurchaseItem =
            PurchaseItem(
                input.id.value ?: Random.nextInt(20, 1000),
                input.text.value,
                input.image.value,
                input.image.value.toString(),
                input.selectedCategory.value,
                input.price.value.toDouble()
            )

        if (input.id.value != null) {
            val editPurchaseItem = purchasesList.indexOfFirst { it.id == input.id.value }
            purchasesList[editPurchaseItem] = newPurchaseItem
        } else {
            purchasesList.add(0, newPurchaseItem)
        }

        Toast.makeText(
            context,
            "Успешно добавихте " + input.text.value,
            Toast.LENGTH_LONG
        ).show()
    }

    resetNewPurchaseInput(input)
}

fun resetNewPurchaseInput(item: BasePurchaseItemInput) {
    item.id.value = PurchaseItemInputInitialValues.id
    item.text.value = PurchaseItemInputInitialValues.text
    item.price.value = PurchaseItemInputInitialValues.price
    item.selectedCategory.value = PurchaseItemInputInitialValues.selectedCategory
    item.image.value = PurchaseItemInputInitialValues.image
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
                demoViewModel.newFuturePurchaseIntent.value = true
            },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_add_24),
                contentDescription = "FAB",
            )
        }
    }
}

