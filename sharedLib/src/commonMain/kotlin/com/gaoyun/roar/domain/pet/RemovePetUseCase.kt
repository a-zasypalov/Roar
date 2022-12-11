package com.gaoyun.roar.domain.pet

import com.gaoyun.roar.domain.interaction.RemoveInteraction
import com.gaoyun.roar.repository.PetRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RemovePetUseCase : KoinComponent {

    private val repository: PetRepository by inject()
    private val removeInteraction: RemoveInteraction by inject()

    fun removePet(petId: String) = flow {
        removeInteraction.removeInteractionByPet(petId).first()
        emit(repository.deletePet(petId))
    }

}