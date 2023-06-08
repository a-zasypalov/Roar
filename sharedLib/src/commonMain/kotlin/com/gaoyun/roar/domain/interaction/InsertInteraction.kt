package com.gaoyun.roar.domain.interaction

import com.gaoyun.roar.model.domain.interactions.Interaction
import com.gaoyun.roar.model.domain.interactions.InteractionRemindConfig
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfig
import com.gaoyun.roar.model.domain.interactions.toInteractionGroup
import com.gaoyun.roar.model.domain.interactions.toInteractionType
import com.gaoyun.roar.repository.InteractionRepository
import kotlinx.coroutines.flow.flow

class InsertInteraction(
    private val repository: InteractionRepository,
) {

    fun insertInteraction(
        templateId: String?,
        petId: String,
        type: String,
        name: String,
        group: String,
        repeatConfig: InteractionRepeatConfig?,
        remindConfig: InteractionRemindConfig,
        notes: String
    ) = flow {
        val newInteraction = Interaction(
            templateId = templateId,
            petId = petId,
            type = type.toInteractionType(),
            name = name,
            group = group.toInteractionGroup(),
            repeatConfig = repeatConfig,
            remindConfig = remindConfig,
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