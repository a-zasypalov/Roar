package com.gaoyun.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = RoarLightGreen,
    primaryVariant = RoarGreen,
    secondary = RoarOrange,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = RoarWhite,
    onSecondary = RoarWhite,
    onBackground = RoarWhite,
    onSurface = RoarWhite
)

private val LightColorPalette = lightColors(
    primary = RoarGreen,
    primaryVariant = RoarDarkGreen,
    secondary = RoarOrange,
    background = RoarLightGray,
    surface = RoarWhite,
    onBackground = RoarBlack,
    onPrimary = RoarWhite,
    onSecondary = RoarWhite,
    onSurface = RoarGreen,
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