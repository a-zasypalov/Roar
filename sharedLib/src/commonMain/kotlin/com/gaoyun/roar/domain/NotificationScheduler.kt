package com.gaoyun.roar.domain

import com.gaoyun.roar.model.domain.NotificationData

interface NotificationScheduler {
    fun scheduleNotification(data: NotificationData)
    fun cancelNotification(id: String?)
}