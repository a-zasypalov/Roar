package com.gaoyun.roar.model.domain.interactions

import com.gaoyun.roar.model.entity.InteractionEntity
import com.gaoyun.roar.util.randomUUID

data class Interaction(
    val id: String = randomUUID(),
    val templateId: String,
    val petId: String,
    val type: InteractionType,
    val name: String,
    val group: InteractionGroup,
    val repeatConfig: InteractionRepeatConfig,
    val isActive: Boolean,
    val notes: String = ""
)

internal fun InteractionEntity.toDomain(): Interaction {
    return Interaction(
        id = id,
        templateId = templateId,
        petId = petId,
        type = type.toInteractionType(),
        name = name,
        group = interactionGroup.toInteractionGroup(),
        repeatConfig = repeatConfig.toInteractionRepeatConfig(),
        isActive = isActive
    )
}