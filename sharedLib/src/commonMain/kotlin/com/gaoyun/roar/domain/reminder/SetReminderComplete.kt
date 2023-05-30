package com.gaoyun.roar.domain.reminder

import com.gaoyun.roar.domain.NotificationScheduler
import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.repository.ReminderRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SetReminderComplete : KoinComponent {

    private val repository: ReminderRepository by inject()
    private val getInteraction: GetInteraction by inject()
    private val getReminder: GetReminder by inject()
    private val removeReminder: RemoveReminder by inject()
    private val notificationScheduler: NotificationScheduler by inject()
    private val addNextReminder: AddNextReminder by inject()

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