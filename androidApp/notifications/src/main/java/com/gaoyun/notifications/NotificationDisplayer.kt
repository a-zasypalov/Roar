package com.gaoyun.notifications

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlin.random.Random

class NotificationDisplayer(
    private val channelProvider: NotificationChannelProvider,
    private val context: Context,
    private val notificationManager: NotificationManagerCompat,
) {

    @SuppressLint("MissingPermission")
    fun display(
        title: String,
        content: String,
        intent: Intent? = null,
        channel: NotificationChannel,
    ) {
        channelProvider.create()

        val notification = NotificationCompat.Builder(context, channel.id)
            .setSmallIcon(com.gaoyun.common.R.drawable.ic_tab_home)
            .setContentText(content)
            .setContentTitle(title)
            .setContentIntent(PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE))
            .build()

        notificationManager.notify(Random.nextInt(), notification)
    }

}