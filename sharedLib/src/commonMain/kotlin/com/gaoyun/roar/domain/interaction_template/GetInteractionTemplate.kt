package com.gaoyun.roar.domain.interaction_template

import com.gaoyun.roar.model.domain.PetType
import com.gaoyun.roar.repository.InteractionTemplateRepository
import kotlinx.coroutines.flow.flow

class GetInteractionTemplate(
    private val repository: InteractionTemplateRepository,
) {

    fun getInteractionTemplate(templateId: String, type: PetType) = flow {
        emit(repository.getInteractionTemplate(templateId, type.toString()))
    }

}