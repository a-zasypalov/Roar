package com.gaoyun.roar.util

import android.app.Activity
import android.app.Application
import android.os.Bundle
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ActivityProvider(application: Application, initialActivity: Activity?) : KoinComponent {
    private val backupHandler: BackupHandler by inject()
    var activeActivity: Activity? = initialActivity

    init {
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activeActivity = activity
                backupHandler.registerExecutor()
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {
                backupHandler.unregisterExecutor()
                activeActivity = null
            }
        })
    }
}