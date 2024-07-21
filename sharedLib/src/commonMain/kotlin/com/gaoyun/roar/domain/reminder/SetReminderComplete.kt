package com.gaoyun.roar.domain.reminder

import com.gaoyun.roar.domain.NotificationScheduler
import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.model.domain.NotificationData
import com.gaoyun.roar.model.domain.NotificationItem
import com.gaoyun.roar.model.domain.interactions.InteractionRemindConfig
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.repository.ReminderRepository
import com.gaoyun.roar.util.randomUUID
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class SetReminderComplete(
    private val getInteraction: GetInteraction,
    private val getReminder: GetReminder,
    private val removeReminder: RemoveReminder,
    private val addNextReminder: AddNextReminder,
    private val notificationScheduler: NotificationScheduler,
    private val repository: ReminderRepository,
    private val insertReminder: InsertReminder
) {

    fun setComplete(id: String, complete: Boolean, completionDateTime: LocalDateTime) = flow {
        repository.setReminderCompleted(id, complete, completionDateTime)

        val newInteractionState = if (complete) {
            addNextReminder.addNextReminder(id)
        } else {
            removeNextReminder(id)
        }

        emit(newInteractionState)
    }

    private suspend fun removeNextReminder(reminderId: String): InteractionWithReminders? {
        val uncompletedReminder = getReminder.getReminder(reminderId).firstOrNull() ?: return null
        val reminderToDelete = getReminder.getReminderByInteraction(uncompletedReminder.interactionId).firstOrNull()
            ?.filter { it.isCompleted.not() && it.id != uncompletedReminder.id }
            ?.maxByOrNull { it.dateTime }

        reminderToDelete?.let { removeReminder.removeReminder(it.id).firstOrNull() }

        val interaction = getNewInteractionState(uncompletedReminder.interactionId) ?: return null
        val workId = uncompletedReminder.notificationJobId ?: randomUUID()

        if(uncompletedReminder.notificationJobId == null) {
            insertReminder.insertReminder(uncompletedReminder.copy(notificationJobId = workId))
        }

        val notificationData = prepareReminder(
            workId = workId,
            reminderDateTime = uncompletedReminder.dateTime,
            reminderId = uncompletedReminder.id,
            remindConfig = interaction.remindConfig
        )
        notificationScheduler.cancelNotification(reminderToDelete?.notificationJobId)
        notificationScheduler.scheduleNotification(notificationData)

        return interaction
    }

    private fun prepareReminder(workId: String, reminderDateTime: LocalDateTime, reminderId: String, remindConfig: InteractionRemindConfig): NotificationData {
        val notificationDateTime = reminderDateTime
            .toInstant(TimeZone.currentSystemDefault())
            .minus(remindConfig.toDuration())
            .toLocalDateTime(TimeZone.currentSystemDefault())

        val notificationData = NotificationData(
            scheduled = notificationDateTime,
            item = NotificationItem.Reminder(
                workId = workId,
                itemId = reminderId
            )
        )

        return notificationData
    }

    private suspend fun getNewInteractionState(interactionId: String) = getInteraction.getInteractionWithReminders(interactionId).firstOrNull()
}