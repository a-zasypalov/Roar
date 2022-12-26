package com.gaoyun.notifications

import androidx.core.app.NotificationManagerCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.gaoyun.roar.model.domain.NotificationData
import com.gaoyun.roar.notification.toInputData
import kotlinx.datetime.toJavaLocalDateTime
import org.koin.core.component.KoinComponent
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

class NotificationScheduler(
    private val workManager: WorkManager,
    private val notificationManager: NotificationManagerCompat
) : KoinComponent {

    fun scheduleNotification(data: NotificationData) {
        if (!notificationManager.areNotificationsEnabled()) return
        scheduleJob(data)
    }

    private fun scheduleJob(data: NotificationData) {
        val windowMin = ChronoUnit.MILLIS.between(LocalDateTime.now(), data.scheduled.toJavaLocalDateTime()).coerceAtLeast(1)
        val request = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(windowMin, TimeUnit.MILLISECONDS)
            .setInputData(data.item.toInputData(data.scheduled))
            .build()

        workManager.enqueueUniqueWork(data.item.workId, ExistingWorkPolicy.REPLACE, request)
    }

}