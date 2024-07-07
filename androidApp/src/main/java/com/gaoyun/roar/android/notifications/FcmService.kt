package com.gaoyun.roar.android.notifications

import android.util.Log
import com.gaoyun.roar.model.domain.NotificationItem
import com.gaoyun.roar.notifications.NotificationHandler
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent

class FcmService : FirebaseMessagingService(), KoinComponent {
    private val handler: NotificationHandler by inject()

    override fun onMessageReceived(message: RemoteMessage) {
        handler.handleImmediate(
            NotificationItem.Push(
                title = message.notification?.title ?: "",
                message = message.notification?.body ?: ""
            )
        )
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("TAG", "Fcm token: $token")
    }
}