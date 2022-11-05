package com.gaoyun.roar.domain.interaction

import com.gaoyun.roar.model.domain.*
import com.gaoyun.roar.model.domain.toInteractionType
import com.gaoyun.roar.repository.InteractionRepository
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class InsertInteraction : KoinComponent {

    private val repository: InteractionRepository by inject()

    fun insertInteraction(
        templateId: String,
        petId: String,
        type: String,
        name: String,
        group: String,
        repeatConfig: InteractionRepeatConfig,
        notes: String
    ) = flow {
        emit(
            repository.insertInteraction(
                Interaction(
                    templateId = templateId,
                    petId = petId,
                    type = type.toInteractionType(),
                    name = name,
                    group = group.toInteractionGroup(),
                    repeatConfig = repeatConfig,
                    isActive = true,
                    notes = notes
                )
            )
        )
    }

}