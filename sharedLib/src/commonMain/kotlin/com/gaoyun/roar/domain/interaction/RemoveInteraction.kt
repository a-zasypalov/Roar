package com.gaoyun.roar.domain.interaction

import com.gaoyun.roar.domain.reminder.RemoveReminder
import com.gaoyun.roar.repository.InteractionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RemoveInteraction : KoinComponent {

    private val repository: InteractionRepository by inject()
    private val removeReminder: RemoveReminder by inject()
    private val getInteraction: GetInteraction by inject()

    fun removeInteraction(id: String) = flow {
        removeReminder.removeReminderByInteraction(id).first()
        emit(repository.deleteInteraction(id))
    }

    fun removeInteractionByPet(petId: String) = flow {
        getInteraction.getInteractionIdsByPet(petId).firstOrNull()?.forEach {
            removeReminder.removeReminderByInteraction(it).firstOrNull()
        }
        emit(repository.deletePetInteractions(petId))
    }

}