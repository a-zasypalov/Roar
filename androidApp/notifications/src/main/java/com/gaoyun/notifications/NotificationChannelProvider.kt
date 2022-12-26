package com.gaoyun.notifications

import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent

class NotificationChannelProvider(
    private val notificationManager: NotificationManagerCompat
) : KoinComponent {

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
                .build()
        }
        notificationManager.createNotificationChannelsCompat(channels)
    }

}

enum class NotificationChannel(val id: String) {
    PetsReminder("com.gaoyun.roar.petsReminder"),
}