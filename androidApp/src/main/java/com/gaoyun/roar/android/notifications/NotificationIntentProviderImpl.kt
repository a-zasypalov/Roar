package com.gaoyun.roar.android.notifications

import android.content.Context
import android.content.Intent
import com.gaoyun.notifications.NotificationIntentProvider
import com.gaoyun.roar.android.MainActivity
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NotificationIntentProviderImpl : NotificationIntentProvider, KoinComponent {
    private val context: Context by inject()
    override fun getDefaultIntent() = Intent(context, MainActivity::class.java)
}