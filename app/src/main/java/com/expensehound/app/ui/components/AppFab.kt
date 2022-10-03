package com.expensehound.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.expensehound.app.R
import com.expensehound.app.ui.viewmodel.MainViewModel
import com.expensehound.app.ui.nav.AppScreens

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


    if (
        demoViewModel.newPurchaseIntent.value && demoViewModel.newPurchaseInput.text.value == ""
        || demoViewModel.newFuturePurchaseIntent.value && demoViewModel.newFuturePurchaseInput.text.value == ""
    ) {
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