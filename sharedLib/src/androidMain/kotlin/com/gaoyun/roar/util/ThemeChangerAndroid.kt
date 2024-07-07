package com.gaoyun.roar.util

import android.content.ComponentName
import android.content.pm.PackageManager
import org.koin.core.component.KoinComponent


class ThemeChangerAndroid(private val activityProvider: ActivityProvider) : KoinComponent, ThemeChanger {
    override fun applyTheme() {
        activityProvider.activeActivity?.recreate()
    }

    override fun activateIcon(icon: AppIcon) {
        activityProvider.activeActivity?.let {
            val enable = when (icon) {
                AppIcon.Roar -> "com.gaoyun.roar.android.ROAR.AMBER"
                AppIcon.Paw -> "com.gaoyun.roar.android.PAW.AMBER"
            }
            val disable = when (icon) {
                AppIcon.Paw -> "com.gaoyun.roar.android.ROAR.AMBER"
                AppIcon.Roar -> "com.gaoyun.roar.android.PAW.AMBER"
            }
            it.packageManager.setComponentEnabledSetting(
                ComponentName(it, disable),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
            )
            it.packageManager.setComponentEnabledSetting(
                ComponentName(it, enable),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
            )
        }
    }
}