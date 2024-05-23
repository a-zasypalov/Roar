package com.gaoyun.roar.android.platform_utils

import android.app.Activity
import android.app.Application
import android.os.Bundle

class ActivityProvider(application: Application, initialActivity: Activity?) {
    var activeActivity: Activity? = initialActivity

    init {
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityResumed(activity: Activity) { activeActivity = activity }
            override fun onActivityPaused(activity: Activity) { activeActivity = null }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }
}