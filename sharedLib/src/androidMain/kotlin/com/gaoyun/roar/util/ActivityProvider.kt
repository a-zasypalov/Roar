package com.gaoyun.roar.util

import android.app.Activity
import org.koin.core.component.KoinComponent

class ActivityProvider(initialActivity: Activity?) : KoinComponent {
    var activeActivity: Activity? = initialActivity
}