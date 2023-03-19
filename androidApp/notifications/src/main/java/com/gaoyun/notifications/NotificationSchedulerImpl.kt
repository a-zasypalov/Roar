package com.gaoyun.notifications

import androidx.core.app.NotificationManagerCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.gaoyun.roar.domain.NotificationScheduler
import com.gaoyun.roar.model.domain.NotificationData
import com.gaoyun.roar.model.domain.NotificationItem
import com.gaoyun.roar.notification.toInputData
import com.gaoyun.roar.util.randomUUID
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import org.koin.core.component.KoinComponent
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit

class NotificationSchedulerImpl(
    private val workManager: WorkManager,
    private val notificationManager: NotificationManagerCompat
) : KoinComponent, NotificationScheduler {

    override fun scheduleNotification(data: NotificationData) {
        if (!notificationManager.areNotificationsEnabled()) return
        if (data.scheduled < LocalDateTime.now().toKotlinLocalDateTime()) return
        scheduleJob(data)
    }

    override fun cancelNotification(id: String?) {
        id?.let {
            workManager.cancelWorkById(UUID.fromString(it))
        }
    }

    private fun scheduleJob(data: NotificationData) {
        val windowMin = ChronoUnit.MILLIS.between(LocalDateTime.now(), data.scheduled.toJavaLocalDateTime()).coerceAtLeast(1)
        val request = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(windowMin, TimeUnit.MILLISECONDS)
            .setInputData(data.item.toInputData(data.scheduled))
            .build()

        workManager.enqueueUniqueWork((data.item as? NotificationItem.Reminder)?.workId ?: randomUUID(), ExistingWorkPolicy.REPLACE, request)
    }

}