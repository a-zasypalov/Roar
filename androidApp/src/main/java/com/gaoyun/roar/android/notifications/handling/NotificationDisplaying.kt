package com.gaoyun.roar.android.notifications.handling

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.gaoyun.roar.android.MainActivity
import com.gaoyun.roar.android.R
import com.gaoyun.roar.notifications.NotificationDisplaying
import kotlin.random.Random

class NotificationDisplayingImpl(
    private val channelProvider: NotificationChannelProvider,
    private val context: Context,
    private val notificationManager: NotificationManagerCompat,
) : NotificationDisplaying {
    private val defaultIntent = Intent(context, MainActivity::class.java)
    private val channel = NotificationChannel.PetsReminder

    @SuppressLint("MissingPermission")
    override fun display(
        title: String,
        content: String,
    ) {
        channelProvider.create()

        val notification = NotificationCompat.Builder(context, channel.id)
            .setSmallIcon(R.drawable.ic_tab_home)
            .setContentText(content)
            .setContentTitle(title)
            .setContentIntent(PendingIntent.getActivity(context, 0, defaultIntent, PendingIntent.FLAG_IMMUTABLE))
            .build()

        notificationManager.notify(Random.nextInt(), notification)
    }
}