package com.gaoyun.roar.android.notifications

import android.content.Context
import android.content.Intent
import com.gaoyun.roar.android.MainActivity

class NotificationIntentProviderImpl(private val context: Context) : NotificationIntentProvider {
    override fun getDefaultIntent() = Intent(context, MainActivity::class.java)
}