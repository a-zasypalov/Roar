package com.gaoyun.feature_create_reminder

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import kotlin.random.Random

class ReminderBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra("textExtra").toString()
        val title = intent.getStringExtra("titleExtra").toString()

        val notification = NotificationCompat.Builder(context, "com.gaoyun.roar.RemindersChannel")
            .setSmallIcon(com.gaoyun.common.R.drawable.ic_tab_home)
            .setContentText(message)
            .setContentTitle(title)
            .build()

        val manager = context.getSystemService(NotificationManager::class.java)
        manager.notify(Random.nextInt(), notification)
    }
}