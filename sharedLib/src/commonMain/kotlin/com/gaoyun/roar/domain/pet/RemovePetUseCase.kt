package com.gaoyun.roar.domain.pet

import com.gaoyun.roar.domain.interaction.RemoveInteraction
import com.gaoyun.roar.repository.PetRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class RemovePetUseCase(
    private val repository: PetRepository,
    private val removeInteraction: RemoveInteraction,
) {

    fun removePet(petId: String) = flow {
        removeInteraction.removeInteractionByPet(petId).first()
        emit(repository.deletePet(petId))
    }

}