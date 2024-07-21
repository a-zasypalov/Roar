package com.gaoyun.roar.ui.common.composables

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

internal object TransparentRippleTheme : RippleTheme {
    private val transparentRippleAlpha = RippleAlpha(
        pressedAlpha = 0.0f,
        focusedAlpha = 0.0f,
        draggedAlpha = 0.0f,
        hoveredAlpha = 0.0f
    )

    @Composable
    override fun defaultColor() = Color.Transparent

    @Composable
    override fun rippleAlpha(): RippleAlpha = transparentRippleAlpha
}

// Usage: CompositionLocalProvider(LocalRippleTheme provides TransparentRippleTheme) {