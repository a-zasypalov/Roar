package com.gaoyun.roar.domain.reminder

import com.gaoyun.roar.domain.NotificationScheduler
import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.model.domain.NotificationData
import com.gaoyun.roar.model.domain.NotificationItem
import com.gaoyun.roar.model.domain.Reminder
import com.gaoyun.roar.model.domain.interactions.InteractionRemindConfig
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfig
import com.gaoyun.roar.repository.ReminderRepository
import com.gaoyun.roar.util.randomUUID
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class InsertReminder(
    private val repository: ReminderRepository,
    private val notificationScheduler: NotificationScheduler,
    private val getInteraction: GetInteraction,
) {

    fun createReminder(interactionId: String, dateTime: LocalDateTime, remindConfig: InteractionRemindConfig) = flow {
        val notificationJobId = randomUUID()

        val newReminder = Reminder(
            interactionId = interactionId,
            dateTime = dateTime,
            notificationJobId = notificationJobId,
        )

        repository.insertReminder(newReminder)

        scheduleNotification(
            notificationDateTime =  getNotificationDateTime(dateTime, remindConfig),
            reminderId = newReminder.id,
            notificationJobId = notificationJobId
        )

        emit(newReminder)
    }

    fun insertReminder(reminder: Reminder) = flow {
        repository.insertReminder(reminder)
        emit(reminder)
    }

    fun insertReminderAndScheduleNotification(reminder: Reminder) = flow {
        val interaction = getInteraction.getInteraction(reminder.interactionId).firstOrNull() ?: return@flow
        val notificationJobId = randomUUID()

        val reminderScheduled = reminder.copy(notificationJobId = notificationJobId)
        repository.insertReminder(reminderScheduled)

        val notificationDateTime = getNotificationDateTime(reminderScheduled.dateTime, interaction.remindConfig)
        scheduleNotification(notificationDateTime = notificationDateTime, reminderId = reminder.id, notificationJobId = notificationJobId)
        emit(reminder)
    }

    private fun getNotificationDateTime(dateTime: LocalDateTime, remindConfig: InteractionRemindConfig): LocalDateTime {
        return dateTime
            .toInstant(TimeZone.currentSystemDefault())
            .minus(remindConfig.toDuration())
            .toLocalDateTime(TimeZone.currentSystemDefault())
    }

    private fun scheduleNotification(notificationDateTime: LocalDateTime, reminderId: String, notificationJobId: String) {
        val data = NotificationData(
            scheduled = notificationDateTime,
            item = NotificationItem.Reminder(itemId = reminderId, workId = notificationJobId)
        )
        notificationScheduler.scheduleNotification(data)
    }

}