package com.gaoyun.common.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.gaoyun.common.theme.colors.BlueColor
import com.gaoyun.common.theme.colors.GreenColor
import com.gaoyun.common.theme.colors.OrangeColor
import com.gaoyun.roar.util.ColorTheme

private val RoarOrange = Color(0xFFee9338)

@Composable
fun RoarTheme(
    userPreferenceDynamicColorsIsActive: Boolean = false,
    colorTheme: ColorTheme = ColorTheme.Orange,
    darkTheme: Boolean = isSystemInDarkTheme(),
    supportsDynamic: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
    content: @Composable () -> Unit
) {
    val colors = if (userPreferenceDynamicColorsIsActive && supportsDynamic) {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        when (colorTheme) {
            ColorTheme.Green -> if (darkTheme) GreenColor.DarkColors else GreenColor.LightColors
            ColorTheme.Blue -> if (darkTheme) BlueColor.DarkColors else BlueColor.LightColors
            ColorTheme.Orange -> if (darkTheme) OrangeColor.DarkColors else OrangeColor.LightColors
        }
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}

fun ColorTheme.primaryColor(darkTheme: Boolean) = when (this) {
    ColorTheme.Green -> if (darkTheme) GreenColor.DarkColors.primary else GreenColor.LightColors.primary
    ColorTheme.Blue -> if (darkTheme) BlueColor.DarkColors.primary else BlueColor.LightColors.primary
    ColorTheme.Orange -> if (darkTheme) OrangeColor.DarkColors.primary else OrangeColor.LightColors.primary
}