package com.gaoyun.roar.domain.interaction_template

import com.gaoyun.roar.model.domain.interactions.InteractionTemplate
import com.gaoyun.roar.repository.InteractionTemplateRepository
import kotlinx.coroutines.flow.flow

class InsertInteractionTemplate(
    private val repository: InteractionTemplateRepository,
) {

    fun insertInteractionTemplate(template: InteractionTemplate) = flow {
        emit(repository.insertInteractionTemplate(template))
    }

    fun insertInteractionTemplates(templates: List<InteractionTemplate>) = flow {
        templates.forEach { repository.insertInteractionTemplate(it) }
        emit(Unit)
    }

}