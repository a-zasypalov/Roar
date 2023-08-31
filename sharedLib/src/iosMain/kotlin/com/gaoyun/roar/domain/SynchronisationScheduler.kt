package com.gaoyun.roar.domain

import com.gaoyun.roar.model.domain.NotificationData

class SynchronisationSchedulerImpl : SynchronisationScheduler {
    override fun scheduleSynchronisation() {
    }
}

class NotificationSchedulerImpl: NotificationScheduler {
    override fun cancelNotification(id: String?) {

    }

    override fun scheduleNotification(data: NotificationData) {

    }
}