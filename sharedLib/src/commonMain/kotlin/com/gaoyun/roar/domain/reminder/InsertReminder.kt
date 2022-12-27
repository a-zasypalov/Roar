package com.gaoyun.roar.domain.reminder

import com.gaoyun.roar.domain.NotificationScheduler
import com.gaoyun.roar.model.domain.NotificationData
import com.gaoyun.roar.model.domain.NotificationItem
import com.gaoyun.roar.model.domain.Reminder
import com.gaoyun.roar.repository.ReminderRepository
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class InsertReminder : KoinComponent {

    private val repository: ReminderRepository by inject()
    private val notificationScheduler: NotificationScheduler by inject()

    fun createReminder(interactionId: String, dateTime: LocalDateTime) = flow {
        val newReminder = Reminder(
            interactionId = interactionId,
            dateTime = dateTime,
        )

        repository.insertReminder(newReminder)

        val data = NotificationData(
            scheduled = dateTime,
            item = NotificationItem.Reminder(itemId = newReminder.id)
        )
        notificationScheduler.scheduleNotification(data)

        emit(newReminder)
    }

    fun insertReminder(reminder: Reminder) = flow {
        repository.insertReminder(reminder)
        emit(reminder)
    }

}