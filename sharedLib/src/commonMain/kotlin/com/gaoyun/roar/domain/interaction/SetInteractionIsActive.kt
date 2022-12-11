package com.gaoyun.roar.domain.interaction

import com.gaoyun.roar.model.domain.interactions.withReminders
import com.gaoyun.roar.repository.InteractionRepository
import com.gaoyun.roar.repository.ReminderRepository
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SetInteractionIsActive : KoinComponent {

    private val repository: InteractionRepository by inject()
    private val reminderRepository: ReminderRepository by inject()

    fun setInteractionIsActive(id: String, isActive: Boolean) = flow {
        repository.setInteractionIsActive(id, isActive)
        repository.getInteraction(id)?.let { interaction ->
            val reminders = reminderRepository.getRemindersByInteraction(interaction.id)

            emit(interaction.withReminders(reminders))
        }
    }

}