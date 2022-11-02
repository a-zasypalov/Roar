package com.gaoyun.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = RoarOrange,
    primaryVariant = RoarOrange,
    secondary = RoarOrange,
    background = DarkSurface,
    surface = DarkSurface,
    onPrimary = RoarWhite,
    onSecondary = RoarWhite,
    onBackground = RoarWhite,
    onSurface = RoarWhite
)

private val LightColorPalette = lightColors(
    primary = RoarOrange,
    primaryVariant = RoarOrange,
    secondary = RoarOrange,
    background = RoarLightGray,
    surface = RoarWhite,
    onBackground = RoarBlack,
    onPrimary = RoarWhite,
    onSecondary = RoarWhite,
    onSurface = RoarBlack,
)

@Composable
fun RoarTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = RoarTypography,
        shapes = Shapes,
        content = content
    )
}