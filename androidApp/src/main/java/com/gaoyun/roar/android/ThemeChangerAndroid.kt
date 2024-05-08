package com.gaoyun.roar.android

import com.gaoyun.roar.util.ThemeChanger
import org.koin.core.component.KoinComponent


class ThemeChangerAndroid(private val activityProvider: ActivityProvider) : KoinComponent, ThemeChanger {
    override fun applyTheme() {
        activityProvider.activeActivity?.recreate()
    }
}