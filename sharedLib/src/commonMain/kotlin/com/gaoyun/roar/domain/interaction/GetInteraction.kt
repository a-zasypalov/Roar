package com.gaoyun.roar.domain.interaction

import com.gaoyun.roar.repository.InteractionRepository
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetInteraction: KoinComponent {

    private val repository: InteractionRepository by inject()

    fun getInteraction(id: String) = flow { emit(repository.getInteraction(id)) }

    fun getInteractionByPet(petId: String) = flow { emit(repository.getInteractionByPet(petId)) }

}