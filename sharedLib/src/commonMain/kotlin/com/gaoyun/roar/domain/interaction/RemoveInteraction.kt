package com.gaoyun.roar.domain.interaction

import com.gaoyun.roar.domain.reminder.RemoveReminder
import com.gaoyun.roar.repository.InteractionRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class RemoveInteraction(
    private val repository: InteractionRepository,
    private val removeReminder: RemoveReminder,
    private val getInteraction: GetInteraction,
) {

    fun removeInteraction(id: String) = flow {
        removeReminder.removeReminderByInteraction(id).firstOrNull()
        emit(repository.deleteInteraction(id))
    }

    fun removeInteractionByPet(petId: String) = flow {
        getInteraction.getInteractionIdsByPet(petId).firstOrNull()?.forEach {
            removeReminder.removeReminderByInteraction(it).firstOrNull()
        }
        emit(repository.deletePetInteractions(petId))
    }

    fun removeInteractionByPetToSync(petId: String) = flow {
        getInteraction.getInteractionIdsByPet(petId).firstOrNull()?.forEach {
            removeReminder.removeReminderByInteraction(it, scheduleSync = false).firstOrNull()
        }
        emit(repository.deletePetInteractions(petId))
    }

}