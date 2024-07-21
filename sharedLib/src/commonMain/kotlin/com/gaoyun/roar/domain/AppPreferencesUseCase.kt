package com.gaoyun.roar.domain

import com.gaoyun.roar.util.Preferences
import com.gaoyun.roar.util.PreferencesKeys
import com.gaoyun.roar.util.PreferencesKeys.HOME_SCREEN_MODE_FULL
import com.gaoyun.roar.util.PreferencesKeys.SHOW_CUSTOMIZATION_PROMPT

class AppPreferencesUseCase(private val prefs: Preferences) {

    fun dynamicColorsIsActive() = prefs.getBoolean(PreferencesKeys.DYNAMIC_COLORS_ACTIVE, true)
    fun setDynamicColors(active: Boolean) = prefs.setBoolean(PreferencesKeys.DYNAMIC_COLORS_ACTIVE, active)
    fun staticTheme() = prefs.getString(PreferencesKeys.COLOR_THEME)
    fun setStaticTheme(theme: String) = prefs.setString(PreferencesKeys.COLOR_THEME, theme)
    fun numberOfRemindersOnMainScreen() = prefs.getInt(PreferencesKeys.NUMBER_OF_REMINDERS_ON_MAIN_SCREEN, 2)
    fun setNumberOfRemindersOnMainScreen(newNumber: Int) = prefs.setInt(PreferencesKeys.NUMBER_OF_REMINDERS_ON_MAIN_SCREEN, newNumber)
    fun homeScreenModeFull() = prefs.getBoolean(HOME_SCREEN_MODE_FULL, true)
    fun switchHomeScreenMode() = prefs.setBoolean(HOME_SCREEN_MODE_FULL, prefs.getBoolean(HOME_SCREEN_MODE_FULL, true).not())
    fun showCustomizationPrompt() = prefs.getBoolean(SHOW_CUSTOMIZATION_PROMPT, true)
    fun closeCustomizationPrompt() = prefs.setBoolean(SHOW_CUSTOMIZATION_PROMPT, false)

}