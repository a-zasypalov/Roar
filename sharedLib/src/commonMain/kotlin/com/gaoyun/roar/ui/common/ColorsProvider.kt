package com.gaoyun.roar.ui.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import com.gaoyun.roar.ui.theme.colors.BlueColor
import com.gaoyun.roar.ui.theme.colors.GreenColor
import com.gaoyun.roar.ui.theme.colors.OrangeColor
import com.gaoyun.roar.util.ColorTheme
import com.gaoyun.roar.util.Preferences
import com.gaoyun.roar.util.PreferencesKeys
import org.koin.core.component.KoinComponent

class ColorsProvider(private val preferences: Preferences) : KoinComponent {

    @Composable
    fun getCurrentScheme(): ColorScheme {
        val colorTheme = ColorTheme.valueOf(preferences.getString(PreferencesKeys.COLOR_THEME, "Orange"))
        val darkTheme = isSystemInDarkTheme()
        val colors = when (colorTheme) {
            ColorTheme.Green -> if (darkTheme) GreenColor.DarkColors else GreenColor.LightColors
            ColorTheme.Blue -> if (darkTheme) BlueColor.DarkColors else BlueColor.LightColors
            ColorTheme.Orange -> if (darkTheme) OrangeColor.DarkColors else OrangeColor.LightColors
        }

        return colors
    }

}