package com.gaoyun.roar.domain.interaction_template

import com.gaoyun.roar.model.domain.PetType
import com.gaoyun.roar.repository.InteractionTemplateRepository
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetInteractionTemplate : KoinComponent {

    private val repository: InteractionTemplateRepository by inject()

    fun getInteractionTemplate(templateId: String, type: PetType) = flow {
        emit(repository.getInteractionTemplate(templateId, type.toString()))
    }

}