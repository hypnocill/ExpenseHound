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
import androidx.compose.runtime.State
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.expensehound.app.R
import com.expensehound.app.ui.navigation.AppScreens
import com.expensehound.app.ui.viewmodel.MainViewModel
import com.expensehound.app.utils.AppAnimationSpecs

@Composable
fun AppFab(viewModel: MainViewModel, backtrace: State<NavBackStackEntry?>) {
    var purchasesFabVisible = false
    var desiresFabVisible = false
    var incomeFabVisible = false

    if (backtrace?.value?.destination?.route == AppScreens.HomeNav.route) {
        when (viewModel.selectedHomeTopAppBarTab.value) {
            0 -> purchasesFabVisible = true
            1 -> desiresFabVisible = true
            2 -> incomeFabVisible = true
        }
    }

    var exitOverrideWithScaleAndFade = false

    if (
        viewModel.newPurchaseIntent.value
        || viewModel.newFuturePurchaseIntent.value
        || viewModel.newIncomeIntent.value
    ) {
        purchasesFabVisible = false
        desiresFabVisible = false
        incomeFabVisible = false
        exitOverrideWithScaleAndFade = true
    }

    AnimatedContainer(
        isVisible = purchasesFabVisible,
        exitOverrideWithScaleAndFade = exitOverrideWithScaleAndFade,
        onClick = { viewModel.newPurchaseIntent.value = true })
    AnimatedContainer(
        isVisible = desiresFabVisible,
        exitOverrideWithScaleAndFade = exitOverrideWithScaleAndFade,
        onClick = { viewModel.newFuturePurchaseIntent.value = true })
    AnimatedContainer(
        isVisible = incomeFabVisible,
        exitOverrideWithScaleAndFade = exitOverrideWithScaleAndFade,
        onClick = { viewModel.newIncomeIntent.value = true })
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