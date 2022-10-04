package com.expensehound.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.expensehound.app.R
import com.expensehound.app.ui.nav.AppScreens
import com.expensehound.app.ui.viewmodel.MainViewModel
import com.expensehound.app.utils.AppAnimationSpecs

@Composable
fun AppFab(navController: NavHostController, demoViewModel: MainViewModel) {
    val backstackEntry = navController.currentBackStackEntryAsState()
    var purchaseFabVisible = false
    var futurePurchaseFabVisible = false

    when(backstackEntry.value?.destination?.route) {
        AppScreens.HomeNav.route -> purchaseFabVisible = true
        AppScreens.Future.route -> futurePurchaseFabVisible = true
    }

    var exitOverrideWithScaleAndFade = false

    if (
        demoViewModel.newPurchaseIntent.value && demoViewModel.newPurchaseInput.text.value == ""
        || demoViewModel.newFuturePurchaseIntent.value && demoViewModel.newFuturePurchaseInput.text.value == ""
    ) {
        purchaseFabVisible = false
        futurePurchaseFabVisible = false
        exitOverrideWithScaleAndFade = true
    }

    AnimatedContainer(isVisible = purchaseFabVisible, exitOverrideWithScaleAndFade = exitOverrideWithScaleAndFade, onClick =  { demoViewModel.newPurchaseIntent.value = true })
    AnimatedContainer(
        isVisible = futurePurchaseFabVisible,
        exitOverrideWithScaleAndFade = exitOverrideWithScaleAndFade,
        onClick = { demoViewModel.newFuturePurchaseIntent.value = true })
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedContainer(
    isVisible: Boolean,
    onClick: () -> Unit,
    enterFromLowerRightCorner: Boolean = false,
    exitToLowerRightCorner: Boolean = false,
    exitOverrideWithScaleAndFade: Boolean = false
) {
    var enterAnimation = scaleIn(
        animationSpec = spring(dampingRatio = AppAnimationSpecs.SPRING_DAMPING_RATIO),
        transformOrigin = if (!enterFromLowerRightCorner) AppAnimationSpecs.TRANSFORM_ORIGIN_CENTER else AppAnimationSpecs.TRANSFORM_ORIGIN_LOWER_RIGHT_CORNER
    )

    var exitAnimation = scaleOut(
        animationSpec = spring(dampingRatio = AppAnimationSpecs.SPRING_DAMPING_RATIO),
        transformOrigin = if (!exitToLowerRightCorner) AppAnimationSpecs.TRANSFORM_ORIGIN_CENTER else AppAnimationSpecs.TRANSFORM_ORIGIN_LOWER_RIGHT_CORNER
    )

    if (exitOverrideWithScaleAndFade) {
        exitAnimation = scaleOut(
            animationSpec = tween(
                durationMillis = AppAnimationSpecs.DURATION, easing = AppAnimationSpecs.CUBIC_EASING
            ),
            transformOrigin = AppAnimationSpecs.TRANSFORM_ORIGIN_LOWER_RIGHT_CORNER,
            targetScale = 4f
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = AppAnimationSpecs.DURATION, easing = AppAnimationSpecs.CUBIC_EASING
            )
        )
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = enterAnimation,
        exit = exitAnimation,
    ) {
        LargeFloatingActionButton(
            onClick = onClick,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_add_24),
                contentDescription = "FAB",
            )
        }
    }
}