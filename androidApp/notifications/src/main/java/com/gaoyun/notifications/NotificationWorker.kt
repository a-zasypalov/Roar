package com.gaoyun.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gaoyun.roar.notification.toNotificationData
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NotificationWorker(context: Context, private val params: WorkerParameters) : CoroutineWorker(context, params), KoinComponent {

    private val handler: NotificationHandler by inject()

    override suspend fun doWork(): Result {
        val notification = params.inputData.toNotificationData()
        val result = handler.handle(notification)
        return if(result) Result.success() else Result.failure()
    }
}