package com.expensehound.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.expensehound.app.R
import com.expensehound.app.ui.theme.margin_half
import com.expensehound.app.utils.AppAnimationSpecs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppFilterChip(text: String, selected: Boolean, onClick: (() -> Unit) = {}) {
    var targetValue = remember { mutableStateOf(0f) }

    val animationProgress by animateFloatAsState(
        targetValue = targetValue.value,
        animationSpec = tween(
            durationMillis = AppAnimationSpecs.DURATION,
            easing = AppAnimationSpecs.CUBIC_EASING
        )
    )

    SideEffect { targetValue.value = 1f }

    FilterChip(
        modifier = Modifier
            .padding(horizontal = margin_half)
            .scale(animationProgress)
            .alpha(0 + animationProgress),
        selected = selected,
        onClick = onClick,
        leadingIcon = {
            if (!selected) {
                return@FilterChip
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_check_24),
                contentDescription = null,
                modifier = Modifier
                    .size(18.dp)
                    .clickable(
                        onClick = onClick,
                        role = Role.Button,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = false, radius = 16.dp),
                    ),
            )
        },
        label = {
            Text(text = text)
        })
}