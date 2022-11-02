package com.gaoyun.roar.model.dto

data class InteractionResponse(
    val id: String,
    val petType: String,
    val type: String,
    val name: String,
    val group: String,
    val repeatConfig: String,
    val additionalFieldsMap: List<InteractionResponseAdditionalField>
)

data class InteractionResponseAdditionalField(
    val key: String,
    val value: String
)
