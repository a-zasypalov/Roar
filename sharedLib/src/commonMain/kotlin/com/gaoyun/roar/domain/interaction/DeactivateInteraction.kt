package com.gaoyun.roar.domain.interaction

import com.gaoyun.roar.model.domain.interactions.withReminders
import com.gaoyun.roar.repository.InteractionRepository
import com.gaoyun.roar.repository.ReminderRepository
import kotlinx.coroutines.flow.flow

class DeactivateInteraction(
    private val repository: InteractionRepository,
    private val reminderRepository: ReminderRepository,
) {

    fun deactivate(id: String) = flow {
        repository.setInteractionIsActive(id, false)
        repository.getInteraction(id)?.let { interaction ->
            val reminders = reminderRepository.getRemindersByInteraction(interaction.id)
            emit(interaction.withReminders(reminders))
        }
    }

}