package com.gaoyun.roar.model.domain

data class Interaction(
    val id: String,
    val templateId: String,
    val petId: String,
    val type: InteractionType,
    val name: String,
    val group: InteractionGroup,
    val repeatConfig: InteractionRepeatConfig,
    val isActive: Boolean
)