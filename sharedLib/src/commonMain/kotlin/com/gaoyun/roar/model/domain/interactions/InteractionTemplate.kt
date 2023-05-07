package com.gaoyun.roar.model.domain.interactions

import com.gaoyun.roar.model.domain.PetType
import com.gaoyun.roar.model.domain.toPetType
import com.gaoyun.roar.model.entity.InteractionTemplateEntity
import com.gaoyun.roar.util.randomUUID
import kotlinx.serialization.Serializable

@Serializable
data class InteractionTemplate(
    val id: String,
    val petType: PetType,
    val type: InteractionType,
    val name: String,
    val group: InteractionGroup,
    val repeatConfig: InteractionRepeatConfig,
) {
    companion object {
        val preview = InteractionTemplate(
            id = randomUUID(),
            petType = PetType.CAT,
            type = InteractionType.CUSTOM,
            name = "Preview Template",
            group = InteractionGroup.CARE,
            repeatConfig = InteractionRepeatConfig(),
        )
    }
}

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