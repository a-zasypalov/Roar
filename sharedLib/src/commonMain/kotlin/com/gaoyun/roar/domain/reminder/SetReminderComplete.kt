package com.gaoyun.roar.domain.reminder

import com.gaoyun.roar.domain.NotificationScheduler
import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.repository.ReminderRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDateTime

class SetReminderComplete(
    private val getInteraction: GetInteraction,
    private val getReminder: GetReminder,
    private val removeReminder: RemoveReminder,
    private val addNextReminder: AddNextReminder,
    private val notificationScheduler: NotificationScheduler,
    private val repository: ReminderRepository,
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

        notificationScheduler.cancelNotification(reminderToDelete?.notificationJobId)

        return getNewInteractionState(uncompletedReminder.interactionId)
    }

    private suspend fun getNewInteractionState(interactionId: String) = getInteraction.getInteractionWithReminders(interactionId).firstOrNull()
}