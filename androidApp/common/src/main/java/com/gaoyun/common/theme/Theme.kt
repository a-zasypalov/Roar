package com.gaoyun.common.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorPalette = darkColorScheme(
    primary = RoarOrange,
    inversePrimary = RoarOrange,
    secondary = RoarOrange,
    background = DarkSurface,
    surface = DarkSurface,
    onPrimary = RoarWhite,
    onSecondary = RoarWhite,
    onBackground = RoarWhite,
    onSurface = RoarWhite
)

private val LightColorPalette = lightColorScheme(
    primary = RoarOrange,
    inversePrimary = RoarOrange,
    secondary = RoarOrange,
    background = RoarLightGray,
    surface = RoarWhite,
    onBackground = RoarBlack,
    onPrimary = RoarWhite,
    onSecondary = RoarWhite,
    onSurface = RoarBlack,
)

@Composable
fun RoarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    supportsDynamic: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
    content: @Composable () -> Unit
) {

    val colors =
        if (supportsDynamic) {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        } else {
            if (darkTheme) DarkColorPalette else LightColorPalette
        }

    MaterialTheme(
        colorScheme = colors,
//        typography = RoarTypography,
//        shapes = Shapes,
        content = content
    )
}