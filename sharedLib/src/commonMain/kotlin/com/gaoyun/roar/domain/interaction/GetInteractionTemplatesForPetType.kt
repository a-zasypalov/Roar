package com.gaoyun.roar.domain.interaction

import com.gaoyun.roar.repository.InteractionTemplateRepository
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetInteractionTemplatesForPetType : KoinComponent {

    private val repository: InteractionTemplateRepository by inject()

    fun getInteractionTemplatesForPetType(type: String) = flow {
        emit(repository.getInteractionTemplatesForPetType(type))
    }

}