package com.gaoyun.roar.util

import android.app.Activity
import android.app.Application
import android.os.Bundle
import org.koin.core.component.KoinComponent

class ActivityProvider(application: Application, initialActivity: Activity?) : KoinComponent {
    var mainActivity: Activity? = initialActivity
    var currentActivity: Activity? = initialActivity

    init {
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                currentActivity = activity
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {
                currentActivity = null
            }
        })
    }
}