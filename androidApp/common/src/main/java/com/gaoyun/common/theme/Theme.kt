package com.gaoyun.common.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.gaoyun.common.theme.generated.DarkColorsGenerated
import com.gaoyun.common.theme.generated.LightColorsGenerated

private val DarkColorPalette = darkColorScheme(
    primary = RoarPrimaryDark,
    onPrimary = RoarOnPrimaryDark,
    primaryContainer = RoarPrimaryContainerDark,
    onPrimaryContainer = RoarOnPrimaryContainerDark,
    inversePrimary = RoarPrimaryDark,
    secondary = RoarPrimaryDark,
    secondaryContainer = RoarPrimaryContainerDark,
    onSecondary = RoarOnPrimaryDark,
    onSecondaryContainer = RoarOnPrimaryContainerDark,
    tertiary = RoarTertiaryDark,
    tertiaryContainer = RoarTertiaryContainerDark,
    onTertiary = RoarOnTertiaryDark,
    onTertiaryContainer = RoarOnTertiaryContainerDark,
    background = RoarBackgroundDark,
    onBackground = RoarOnBackgroundDark,
    error = RoarErrorDark,
    errorContainer = RoarErrorContainerDark,
    onError = RoarOnErrorDark,
    onErrorContainer = RoarOnErrorContainerDark,
    outline = RoarOutlineDark,
    surface = RoarSurfaceDark,
    onSurface = RoarOnSurfaceDark,
    surfaceVariant = RoarSurfaceVariantDark,
    onSurfaceVariant = RoarOnSurfaceVariantDark,
    inverseSurface = RoarSurfaceInverseDark,
    inverseOnSurface = RoarOnSurfaceInverseDark
)

private val LightColorPalette = lightColorScheme(
    primary = RoarPrimary,
    onPrimary = RoarOnPrimary,
    primaryContainer = RoarPrimaryContainer,
    onPrimaryContainer = RoarOnPrimaryContainer,
    inversePrimary = RoarPrimary,
    secondary = RoarPrimary,
    secondaryContainer = RoarPrimaryContainer,
    onSecondary = RoarOnPrimary,
    onSecondaryContainer = RoarOnPrimaryContainer,
    tertiary = RoarTertiary,
    tertiaryContainer = RoarTertiaryContainer,
    onTertiary = RoarOnTertiary,
    onTertiaryContainer = RoarOnTertiaryContainer,
    background = RoarBackground,
    onBackground = RoarOnBackground,
    error = RoarError,
    errorContainer = RoarErrorContainer,
    onError = RoarOnError,
    onErrorContainer = RoarOnErrorContainer,
    outline = RoarOutline,
    surface = RoarSurface,
    onSurface = RoarOnSurface,
    surfaceVariant = RoarSurfaceVariant,
    onSurfaceVariant = RoarOnSurfaceVariant,
    inverseSurface = RoarSurfaceInverse,
    inverseOnSurface = RoarOnSurfaceInverse
)

@Composable
fun RoarTheme(
    userPreferenceDynamicColorsIsActive: Boolean = false,
    darkTheme: Boolean = isSystemInDarkTheme(),
    supportsDynamic: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
    content: @Composable () -> Unit
) {
    val colors = if (userPreferenceDynamicColorsIsActive && supportsDynamic) {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        if (darkTheme) DarkColorsGenerated else LightColorsGenerated
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}