package com.gaoyun.roar.notifications

import com.gaoyun.roar.model.domain.NotificationData
import com.gaoyun.roar.model.domain.NotificationItem

class NotificationHandler(
    private val maker: NotificationContentMaker,
    private val displaying: NotificationDisplaying,
) {
    suspend fun handle(notification: NotificationData): Boolean {
        return when (notification.item) {
            is NotificationItem.Reminder -> {
                maker.make(notification.item.itemId)?.let { (title, content) ->
                    displaying.display(title, content); true
                } ?: false
            }

            is NotificationItem.Push -> {
                handleImmediate(notification.item); true
            }
        }
    }

    fun handleImmediate(notification: NotificationItem.Push) {
        displaying.display(title = notification.title, content = notification.message)
    }
}