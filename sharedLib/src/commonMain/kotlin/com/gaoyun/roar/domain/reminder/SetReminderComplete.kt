package com.gaoyun.roar.domain.reminder

import com.gaoyun.roar.domain.NotificationScheduler
import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.domain.interaction.SetInteractionIsActive
import com.gaoyun.roar.domain.repeat_config.RepeatConfigUseCase
import com.gaoyun.roar.model.domain.NotificationData
import com.gaoyun.roar.model.domain.NotificationItem
import com.gaoyun.roar.model.domain.Reminder
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.repository.ReminderRepository
import com.gaoyun.roar.util.randomUUID
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SetReminderComplete : KoinComponent {

    private val repository: ReminderRepository by inject()
    private val getInteraction: GetInteraction by inject()
    private val getReminder: GetReminder by inject()
    private val removeReminder: RemoveReminder by inject()
    private val insertReminder: InsertReminder by inject()
    private val repeatConfigUseCase: RepeatConfigUseCase by inject()
    private val setInteractionIsActive: SetInteractionIsActive by inject()
    private val notificationScheduler: NotificationScheduler by inject()

    fun setComplete(id: String, complete: Boolean, completionDateTime: LocalDateTime) = flow {
        repository.setReminderCompleted(id, complete, completionDateTime)

        val newInteractionState = if (complete) {
            addNextReminder(id)
        } else {
            removeNextReminder(id)
        }

        emit(newInteractionState)
    }

    private suspend fun addNextReminder(reminderId: String): InteractionWithReminders? {
        val completedReminder = getReminder.getReminder(reminderId).firstOrNull() ?: return null
        notificationScheduler.cancelNotification(completedReminder.notificationJobId)

        val interaction = getInteraction.getInteraction(completedReminder.interactionId).firstOrNull() ?: return null

        if (interaction.repeatConfig != null && interaction.isActive) {
            repeatConfigUseCase.getNextDateAccordingToRepeatConfig(
                repeatConfig = interaction.repeatConfig,
                interactionId = interaction.id,
                from = completedReminder.dateTime.date
            )?.atTime(completedReminder.dateTime.hour, completedReminder.dateTime.minute)?.let { nextReminderDateTime ->
                val newReminderId = randomUUID()
                val newNotificationJobId = scheduleNextReminder(
                    dateTime = nextReminderDateTime,
                    reminderId = newReminderId
                )
                val newReminder = Reminder(
                    id = newReminderId,
                    interactionId = interaction.id,
                    dateTime = nextReminderDateTime,
                    notificationJobId = newNotificationJobId
                )

                insertReminder.insertReminder(newReminder).firstOrNull()
            } ?: setInteractionIsActive.setInteractionIsActive(interaction.id, isActive = false).firstOrNull()
        }

        return getNewInteractionState(interaction.id)
    }

    private suspend fun removeNextReminder(reminderId: String): InteractionWithReminders? {
        val uncompletedReminder = getReminder.getReminder(reminderId).firstOrNull() ?: return null
        val reminderToDelete = getReminder.getReminderByInteraction(uncompletedReminder.interactionId).firstOrNull()
            ?.filter { it.isCompleted.not() && it.id != uncompletedReminder.id }
            ?.maxByOrNull { it.dateTime }

        reminderToDelete?.let { removeReminder.removeReminder(it.id).firstOrNull() }

        notificationScheduler.cancelNotification(reminderToDelete?.notificationJobId)

        return getNewInteractionState(uncompletedReminder.interactionId)
    }

    private suspend fun getNewInteractionState(interactionId: String) = getInteraction.getInteractionWithReminders(interactionId).firstOrNull()

    private fun scheduleNextReminder(dateTime: LocalDateTime, reminderId: String): String {
        val notificationData = NotificationData(
            scheduled = dateTime,
            item = NotificationItem.Reminder(
                itemId = reminderId
            )
        )
        notificationScheduler.scheduleNotification(notificationData)

        return (notificationData.item as? NotificationItem.Reminder)?.workId ?: randomUUID()
    }
}