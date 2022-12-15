package com.gaoyun.roar.domain.interaction

import com.gaoyun.roar.model.domain.interactions.Interaction
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfig
import com.gaoyun.roar.model.domain.interactions.toInteractionGroup
import com.gaoyun.roar.model.domain.interactions.toInteractionType
import com.gaoyun.roar.repository.InteractionRepository
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class InsertInteraction : KoinComponent {

    private val repository: InteractionRepository by inject()

    fun insertInteraction(
        templateId: String?,
        petId: String,
        type: String,
        name: String,
        group: String,
        repeatConfig: InteractionRepeatConfig?,
        notes: String
    ) = flow {
        val newInteraction = Interaction(
            templateId = templateId,
            petId = petId,
            type = type.toInteractionType(),
            name = name,
            group = group.toInteractionGroup(),
            repeatConfig = repeatConfig,
            isActive = true,
            notes = notes
        )

        repository.insertInteraction(newInteraction)

        emit(newInteraction)
    }

    fun insertInteraction(interaction: Interaction) = flow {
        repository.insertInteraction(interaction)
        emit(interaction)
    }

}