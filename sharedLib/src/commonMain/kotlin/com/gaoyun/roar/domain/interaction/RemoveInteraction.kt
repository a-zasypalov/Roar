package com.gaoyun.roar.domain.interaction

import com.gaoyun.roar.repository.InteractionRepository
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RemoveInteraction: KoinComponent {

    private val repository: InteractionRepository by inject()

    fun removeInteraction(id: String) = flow { emit(repository.deleteInteraction(id)) }

    fun removeInteractionByPet(petId: String) = flow { emit(repository.deletePetInteractions(petId)) }

}