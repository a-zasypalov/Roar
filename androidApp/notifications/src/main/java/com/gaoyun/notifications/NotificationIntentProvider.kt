package com.gaoyun.notifications

import android.content.Intent

interface NotificationIntentProvider {
    fun getDefaultIntent(): Intent
}