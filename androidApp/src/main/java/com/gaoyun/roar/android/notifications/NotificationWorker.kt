package com.gaoyun.roar.android.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gaoyun.roar.notification.toNotificationData

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