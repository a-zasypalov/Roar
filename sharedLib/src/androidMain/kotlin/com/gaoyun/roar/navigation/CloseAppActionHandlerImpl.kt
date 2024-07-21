package com.gaoyun.roar.navigation

import com.gaoyun.roar.ui.navigation.CloseAppActionHandler
import com.gaoyun.roar.util.ActivityProvider
import org.koin.core.component.KoinComponent

class CloseAppActionHandlerImpl(private val activityProvider: ActivityProvider) : KoinComponent, CloseAppActionHandler {
    override fun closeApp() {
        activityProvider.mainActivity?.finishAffinity()
        activityProvider.currentActivity?.finishAffinity()
    }
}