package com.gaoyun.roar.domain.interaction

import com.gaoyun.roar.model.domain.interactions.withReminders
import com.gaoyun.roar.repository.InteractionRepository
import com.gaoyun.roar.repository.ReminderRepository
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetInteraction : KoinComponent {

    private val repository: InteractionRepository by inject()
    private val reminderRepository: ReminderRepository by inject()

    fun getInteraction(id: String) = flow { emit(repository.getInteraction(id)) }

    fun getInteractionByPet(petId: String) = flow {
        val interactions = repository.getInteractionByPet(petId).map {
            val reminders = reminderRepository.getRemindersByInteraction(it.id)
            it.withReminders(reminders)
        }

        emit(interactions)
    }

}