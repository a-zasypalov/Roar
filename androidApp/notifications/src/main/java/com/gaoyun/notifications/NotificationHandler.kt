package com.gaoyun.notifications

import android.content.Intent
import com.gaoyun.roar.model.domain.NotificationData
import org.koin.core.component.KoinComponent

class NotificationHandler(
    private val displayer: NotificationDisplayer
) : KoinComponent {

    fun handle(notification: NotificationData) {
        displayer.display(
            title = "",
            content = "",
            intent = Intent(),
            channel = NotificationChannel.PetsReminder
        )
    }


}