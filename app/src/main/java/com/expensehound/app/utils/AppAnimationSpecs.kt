package com.expensehound.app.utils

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.ui.graphics.TransformOrigin

class AppAnimationSpecs() {
    companion object {
        const val DURATION = 200
        val SPRING_DAMPING_RATIO = 1.5f

        val CUBIC_EASING = CubicBezierEasing(0.42f, 0.0f, 0.58f, 1.0f)
        val TRANSFORM_ORIGIN_LOWER_RIGHT_CORNER = TransformOrigin(0.95f, 0.95f)
        val TRANSFORM_ORIGIN_CENTER = TransformOrigin(0.5f, 0.5f)

    }
}
