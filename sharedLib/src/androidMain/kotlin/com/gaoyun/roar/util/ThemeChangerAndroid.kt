package com.gaoyun.roar.util

import android.app.Activity
import android.content.ComponentName
import android.content.pm.PackageManager
import org.koin.core.component.KoinComponent


class ThemeChangerAndroid(private val activityProvider: ActivityProvider) : KoinComponent, ThemeChanger {
    override fun applyTheme() {
        activityProvider.currentActivity?.recreate()
        activityProvider.mainActivity?.recreate()
    }

    override fun activateIcon(icon: AppIcon) {
        activityProvider.currentActivity?.let { tryActivateIcon(it, icon) }
        activityProvider.mainActivity?.let { tryActivateIcon(it, icon) }
    }

    private fun tryActivateIcon(activity: Activity, icon: AppIcon) {
        val enable = when (icon) {
            AppIcon.Roar -> "com.gaoyun.roar.android.ROAR.AMBER"
            AppIcon.Paw -> "com.gaoyun.roar.android.PAW.AMBER"
        }
        val disable = when (icon) {
            AppIcon.Paw -> "com.gaoyun.roar.android.ROAR.AMBER"
            AppIcon.Roar -> "com.gaoyun.roar.android.PAW.AMBER"
        }
        activity.packageManager.setComponentEnabledSetting(
            ComponentName(activity, disable),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
        )
        activity.packageManager.setComponentEnabledSetting(
            ComponentName(activity, enable),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
        )
    }
}