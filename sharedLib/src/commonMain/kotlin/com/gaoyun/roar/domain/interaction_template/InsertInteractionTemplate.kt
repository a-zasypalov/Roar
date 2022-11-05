package com.gaoyun.roar.domain.interaction_template

import com.gaoyun.roar.model.domain.InteractionTemplate
import com.gaoyun.roar.repository.InteractionTemplateRepository
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class InsertInteractionTemplate : KoinComponent {

    private val repository: InteractionTemplateRepository by inject()

    fun insertInteractionTemplate(template: InteractionTemplate) = flow {
        emit(repository.insertInteractionTemplate(template))
    }

    fun insertInteractionTemplates(templates: List<InteractionTemplate>) = flow {
        templates.forEach { repository.insertInteractionTemplate(it) }
        emit(Unit)
    }

}