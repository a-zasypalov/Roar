package com.gaoyun.roar.android.notifications

import android.content.Intent

interface NotificationIntentProvider {
    fun getDefaultIntent(): Intent
}