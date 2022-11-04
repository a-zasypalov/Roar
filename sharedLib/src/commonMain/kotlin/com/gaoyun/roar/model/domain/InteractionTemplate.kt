package com.gaoyun.roar.model.domain

import com.gaoyun.roar.model.entity.InteractionTemplateEntity

data class InteractionTemplate(
    val id: String,
    val petType: PetType,
    val type: InteractionType,
    val name: String,
    val group: InteractionGroup,
    val repeatConfig: InteractionRepeatConfig,
    val additionalFields: List<Pair<String, String>>
)

fun InteractionTemplateEntity.toDomain(): InteractionTemplate {
    return InteractionTemplate(
        id = id,
        petType = petType.toPetType(),
        type = type.toInteractionType(),
        name = name,
        group = interactionGroup.toInteractionGroup(),
        repeatConfig = repeatConfig.toInteractionRepeatConfig(),
        additionalFields = additionalFields.decodeAdditionalFields()
    )
}

private fun String.decodeAdditionalFields() =
    if (this.isEmpty()) {
        listOf()
    } else {
        this.split(",").map { item ->
            val split = item.split(":")
            split[0] to split[1]
        }
    }

internal fun List<Pair<String, String>>.encodeAdditionalFields() = this.joinToString(separator = ",") { "${it.first}:${it.second}" }