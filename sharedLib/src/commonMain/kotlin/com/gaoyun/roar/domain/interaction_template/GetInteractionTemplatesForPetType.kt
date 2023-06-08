package com.gaoyun.roar.domain.interaction_template

import com.gaoyun.roar.model.domain.PetType
import com.gaoyun.roar.repository.InteractionTemplateRepository
import kotlinx.coroutines.flow.flow

class GetInteractionTemplatesForPetType(
    private val repository: InteractionTemplateRepository,
) {

    fun getInteractionTemplatesForPetType(type: PetType) = flow {
        emit(repository.getInteractionTemplatesForPetType(type.toString()))
    }

}