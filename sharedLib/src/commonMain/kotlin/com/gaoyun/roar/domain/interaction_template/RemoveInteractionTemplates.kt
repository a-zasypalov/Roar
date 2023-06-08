package com.gaoyun.roar.domain.interaction_template

import com.gaoyun.roar.repository.InteractionTemplateRepository
import kotlinx.coroutines.flow.flow

class RemoveInteractionTemplates(
    private val repository: InteractionTemplateRepository,
) {

    fun removeInteractionTemplates() = flow { emit(repository.deleteAllTemplates()) }

}