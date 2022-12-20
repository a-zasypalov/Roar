package com.gaoyun.roar.domain.reminder

import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.domain.interaction.SetInteractionIsActive
import com.gaoyun.roar.domain.repeat_config.RepeatConfigUseCase
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.repository.ReminderRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
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

        if (interaction.repeatConfig != null && interaction.isActive) {
            repeatConfigUseCase.getNextDateAccordingToRepeatConfig(
                repeatConfig = interaction.repeatConfig,
                interactionId = interaction.id,
                from = completedReminder.dateTime.date
            )?.atTime(completedReminder.dateTime.hour, completedReminder.dateTime.minute)?.let { nextReminderDateTime ->
                insertReminder.insertReminder(interaction.id, nextReminderDateTime).firstOrNull()
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

        return getNewInteractionState(uncompletedReminder.interactionId)
    }

    private suspend fun getNewInteractionState(interactionId: String) = getInteraction.getInteractionWithReminders(interactionId).firstOrNull()

}