package com.gaoyun.roar.android.notifications

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.gaoyun.roar.domain.NotificationScheduler
import com.gaoyun.roar.model.domain.NotificationData
import com.gaoyun.roar.model.domain.NotificationItem
import com.gaoyun.roar.notification.toInputData
import com.gaoyun.roar.notification.toNotificationData
import com.gaoyun.roar.notifications.NotificationHandler
import com.gaoyun.roar.util.randomUUID
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID
import java.util.concurrent.TimeUnit

const val NOTIFICATION_WORK_TAG = "NOTIFICATION_WORK_TAG"

class NotificationSchedulerImpl(
    private val workManager: WorkManager,
    private val notificationManager: NotificationManagerCompat
) : NotificationScheduler {
    override fun scheduleNotification(data: NotificationData) {
        if (!notificationManager.areNotificationsEnabled()) return

        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        Log.d("NotificationScheduler", "Attempt to schedule notification: ${data.scheduled}, now: $now")

        if (data.scheduled < now) return
        scheduleJob(data)
    }

    override fun cancelNotification(id: String?) {
        id?.let { workManager.cancelUniqueWork(it) }
        Log.d("NotificationScheduler", "Cancelling job: $id")
    }

    override fun cancelAllNotifications() {
        workManager.cancelAllWorkByTag(NOTIFICATION_WORK_TAG)
        Log.d("NotificationScheduler", "Cancelling all jobs")
    }

    override fun cancelNotifications(ids: List<String>) {
        ids.forEach { workManager.cancelUniqueWork(it) }
        Log.d("NotificationScheduler", "Cancelling jobs: $ids")
    }

    override fun scheduledNotificationIds(completion: (List<String>) -> Unit) {
        val jobs = workManager.getWorkInfosByTag(NOTIFICATION_WORK_TAG).get()
        val ids = jobs.filter { it.state == WorkInfo.State.ENQUEUED }.map { it.id.toString() }
        completion(ids)
    }

    private fun scheduleJob(data: NotificationData) {
        val workId = UUID.fromString((data.item as? NotificationItem.Reminder)?.workId ?: randomUUID())
        val windowMin = ChronoUnit.MILLIS.between(LocalDateTime.now(), data.scheduled.toJavaLocalDateTime()).coerceAtLeast(1)
        val request = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setId(workId)
            .addTag(NOTIFICATION_WORK_TAG)
            .setInitialDelay(windowMin, TimeUnit.MILLISECONDS)
            .setInputData(data.item.toInputData(data.scheduled))
            .build()

        Log.d("NotificationScheduler", "Scheduled notification: ${data.scheduled} id:$workId")
        workManager.enqueueUniqueWork(workId.toString(), ExistingWorkPolicy.REPLACE, request)
    }

    class NotificationWorker(
        context: Context,
        private val params: WorkerParameters,
        private val handler: NotificationHandler
    ) : CoroutineWorker(context, params) {
        override suspend fun doWork(): Result {
            val notification = params.inputData.toNotificationData()
            val result = handler.handle(notification)
            return if (result) Result.success() else Result.failure()
        }
    }
}