package com.gaoyun.roar.android.notifications

import android.media.AudioAttributes
import android.media.RingtoneManager
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.runBlocking

class NotificationChannelProvider(
    private val notificationManager: NotificationManagerCompat
) {

    private var created = false

    fun create() {
        if (created) return

        createChannels()
        created = true
    }

    private fun createChannels() = runBlocking {
        val channels = NotificationChannel.values().map { channel ->
            val title = "Roar"
            NotificationChannelCompat.Builder(channel.id, NotificationManagerCompat.IMPORTANCE_HIGH)
                .setName(title)
                .setShowBadge(false)
                .setVibrationEnabled(true)
                .setSound(
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build()
                )
                .build()
        }
        notificationManager.createNotificationChannelsCompat(channels)
    }

}

enum class NotificationChannel(val id: String) {
    PetsReminder("com.gaoyun.roar.petsReminder"),
    Push("com.gaoyun.roar.push"),
}