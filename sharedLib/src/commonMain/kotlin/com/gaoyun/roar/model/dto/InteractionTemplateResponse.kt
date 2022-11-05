package com.gaoyun.roar.model.dto

import com.gaoyun.roar.model.domain.interactions.InteractionTemplate
import com.gaoyun.roar.model.domain.interactions.toInteractionGroup
import com.gaoyun.roar.model.domain.interactions.toInteractionRepeatConfig
import com.gaoyun.roar.model.domain.interactions.toInteractionType
import com.gaoyun.roar.model.domain.toPetType
import kotlinx.serialization.Serializable

@Serializable
internal data class InteractionTemplatesListResponse(
   val items: List<InteractionTemplateResponse>
)

@Serializable
internal data class InteractionTemplateResponse(
    val id: String,
    val name: String,
    val petType: String,
    val type: String,
    val interactionGroup: String,
    val repeatConfig: String,
)

internal fun InteractionTemplateResponse.toDomain(): InteractionTemplate {
    return InteractionTemplate(
        id = id,
        petType = petType.toPetType(),
        type = type.toInteractionType(),
        name = name,
        group = interactionGroup.toInteractionGroup(),
        repeatConfig = repeatConfig.toInteractionRepeatConfig(),
    )
}