package com.gaoyun.roar.domain

import com.gaoyun.roar.model.domain.NotificationData

interface NotificationScheduler {
    fun scheduledNotificationIds(completion: (List<String>) -> Unit)
    fun scheduleNotification(data: NotificationData)
    fun cancelNotification(id: String?)
    fun cancelNotifications(ids: List<String>)
    fun cancelAllNotifications()
}