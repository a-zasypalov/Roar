package com.gaoyun.roar.domain.reminder

import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.repository.ReminderRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.days

class SetReminderComplete : KoinComponent {

    private val repository: ReminderRepository by inject()
    private val getInteraction: GetInteraction by inject()
    private val getReminder: GetReminder by inject()
    private val removeReminder: RemoveReminder by inject()
    private val insertReminder: InsertReminder by inject()

    fun setComplete(id: String, complete: Boolean) = flow {
        repository.setReminderCompleted(id, complete)

        val newInteractionState = if (complete) {
            addNextReminder(id)
        } else {
            removeNextReminder(id)
        }

        emit(newInteractionState)
    }

    private suspend fun addNextReminder(reminderId: String): InteractionWithReminders? {
        val completedReminder = getReminder.getReminder(reminderId).firstOrNull() ?: return null
        val interaction = getInteraction.getInteraction(completedReminder.interactionId).firstOrNull() ?: return null

        if (interaction.isActive) {
            //TODO: Make RepeatConfig dependent
            val nextReminderDateTime = completedReminder.dateTime
                .toInstant(TimeZone.currentSystemDefault())
                .plus(1.days)
                .toLocalDateTime(TimeZone.currentSystemDefault())

            insertReminder.insertReminder(interaction.id, nextReminderDateTime).firstOrNull() ?: return null
        }

        return getNewInteractionState(interaction.id)
    }

    private suspend fun removeNextReminder(reminderId: String): InteractionWithReminders? {
        val uncompletedReminder = getReminder.getReminder(reminderId).firstOrNull() ?: return null
        val reminderToDelete = getReminder.getReminderByInteraction(uncompletedReminder.interactionId).firstOrNull()
            ?.filter { it.isCompleted.not() }
            ?.maxByOrNull { it.dateTime } ?: return null

        removeReminder.removeReminder(reminderToDelete.id).firstOrNull()

        return getNewInteractionState(uncompletedReminder.interactionId)
    }

    private suspend fun getNewInteractionState(interactionId: String) = getInteraction.getInteractionWithReminders(interactionId).firstOrNull()

}