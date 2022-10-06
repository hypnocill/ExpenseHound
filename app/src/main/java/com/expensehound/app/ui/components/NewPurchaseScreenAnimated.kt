package com.expensehound.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.TransformOrigin
import com.expensehound.app.ui.screens.new_purchase.NewPurchaseScreen
import com.expensehound.app.ui.viewmodel.BasePurchaseItemInput
import com.expensehound.app.utils.AppAnimationSpecs

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NewPurchaseScreenAnimated(
    isVisible: Boolean,
    input: BasePurchaseItemInput,
    purchaseIntent: MutableState<Boolean>
) {
    val isInputEmpty = input.text.value == ""

    val animationOrigin = if (isInputEmpty) 0.95f else 0.50f
    val transformOrigin = TransformOrigin(animationOrigin, animationOrigin)
    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(
            animationSpec = tween(
                durationMillis = AppAnimationSpecs.DURATION, easing = AppAnimationSpecs.CUBIC_EASING
            ), transformOrigin = transformOrigin
        ) + fadeIn(),
        exit = scaleOut(
            animationSpec = tween(
                durationMillis = AppAnimationSpecs.DURATION, easing = AppAnimationSpecs.CUBIC_EASING
            ),
            transformOrigin = transformOrigin,
        ) + fadeOut(),
    ) {
        NewPurchaseScreen(input, purchaseIntent)
    }
}