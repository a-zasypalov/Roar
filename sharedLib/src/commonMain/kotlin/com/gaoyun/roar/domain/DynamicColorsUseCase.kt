package com.gaoyun.roar.domain

import com.gaoyun.roar.util.Preferences
import com.gaoyun.roar.util.PreferencesKeys
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DynamicColorsUseCase : KoinComponent {

    private val prefs: Preferences by inject()

    fun dynamicColorsIsActive() = prefs.getBoolean(PreferencesKeys.DYNAMIC_COLORS_ACTIVE, false)

    fun setDynamicColors(active: Boolean) = prefs.setBoolean(PreferencesKeys.DYNAMIC_COLORS_ACTIVE, active)

}