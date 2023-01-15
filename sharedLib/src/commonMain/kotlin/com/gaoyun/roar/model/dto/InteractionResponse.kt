package com.gaoyun.roar.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class InteractionResponse(
    val id: String,
    val petType: String,
    val type: String,
    val name: String,
    val group: String,
    val repeatConfig: String,
    val additionalFieldsMap: List<InteractionResponseAdditionalField>
)

@Serializable
data class InteractionResponseAdditionalField(
    val key: String,
    val value: String
)
