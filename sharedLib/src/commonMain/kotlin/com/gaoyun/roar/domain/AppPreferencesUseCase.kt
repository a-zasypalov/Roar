package com.gaoyun.roar.domain

import com.gaoyun.roar.util.Preferences
import com.gaoyun.roar.util.PreferencesKeys
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AppPreferencesUseCase : KoinComponent {

    private val prefs: Preferences by inject()

    fun dynamicColorsIsActive() = prefs.getBoolean(PreferencesKeys.DYNAMIC_COLORS_ACTIVE, false)
    fun setDynamicColors(active: Boolean) = prefs.setBoolean(PreferencesKeys.DYNAMIC_COLORS_ACTIVE, active)
    fun staticTheme() = prefs.getString(PreferencesKeys.COLOR_THEME)
    fun setStaticTheme(theme: String) = prefs.setString(PreferencesKeys.COLOR_THEME, theme)
    fun numberOfRemindersOnMainScreen() = prefs.getInt(PreferencesKeys.NUMBER_OF_REMINDERS_ON_MAIN_SCREEN, 2)
    fun setNumberOfRemindersOnMainScreen(newNumber: Int) = prefs.setInt(PreferencesKeys.NUMBER_OF_REMINDERS_ON_MAIN_SCREEN, newNumber)

}