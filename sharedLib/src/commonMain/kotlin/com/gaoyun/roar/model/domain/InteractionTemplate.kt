package com.gaoyun.roar.model.domain

import com.gaoyun.roar.model.entity.InteractionTemplateEntity

data class InteractionTemplate(
    val id: String,
    val petType: PetType,
    val type: InteractionType,
    val name: String,
    val group: InteractionGroup,
    val repeatConfig: InteractionRepeatConfig,
)

fun InteractionTemplateEntity.toDomain(): InteractionTemplate {
    return InteractionTemplate(
        id = id,
        petType = petType.toPetType(),
        type = type.toInteractionType(),
        name = name,
        group = interactionGroup.toInteractionGroup(),
        repeatConfig = repeatConfig.toInteractionRepeatConfig(),
    )
}