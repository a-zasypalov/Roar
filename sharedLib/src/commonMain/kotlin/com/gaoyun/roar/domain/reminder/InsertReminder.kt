package com.gaoyun.roar.domain.reminder

import com.gaoyun.roar.domain.NotificationScheduler
import com.gaoyun.roar.model.domain.NotificationData
import com.gaoyun.roar.model.domain.NotificationItem
import com.gaoyun.roar.model.domain.Reminder
import com.gaoyun.roar.model.domain.interactions.InteractionRemindConfig
import com.gaoyun.roar.repository.ReminderRepository
import com.gaoyun.roar.util.randomUUID
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class InsertReminder : KoinComponent {

    private val repository: ReminderRepository by inject()
    private val notificationScheduler: NotificationScheduler by inject()

    fun createReminder(interactionId: String, dateTime: LocalDateTime, remindConfig: InteractionRemindConfig) = flow {
        val notificationJobId = randomUUID()

        val newReminder = Reminder(
            interactionId = interactionId,
            dateTime = dateTime,
            notificationJobId = notificationJobId,
        )

        repository.insertReminder(newReminder)

        val notificationDateTime = dateTime
            .toInstant(TimeZone.currentSystemDefault())
            .minus(remindConfig.toDuration())
            .toLocalDateTime(TimeZone.currentSystemDefault())

        val data = NotificationData(
            scheduled = notificationDateTime,
            item = NotificationItem.Reminder(itemId = newReminder.id, workId = notificationJobId)
        )
        notificationScheduler.scheduleNotification(data)

        emit(newReminder)
    }

    fun insertReminder(reminder: Reminder) = flow {
        repository.insertReminder(reminder)
        emit(reminder)
    }

}