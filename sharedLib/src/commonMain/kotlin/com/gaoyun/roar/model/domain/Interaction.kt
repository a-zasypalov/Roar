package com.gaoyun.roar.model.domain

import com.gaoyun.roar.util.randomUUID

sealed interface Interaction {

    val id: String
    val templateId: String
    val petId: String
    val type: InteractionType
    val name: String
    val group: InteractionGroup
    val repeatConfig: InteractionRepeatConfig
    val isActive: Boolean

    data class Deworming(
        override val id: String = randomUUID(),
        override val templateId: String,
        override val petId: String,
        override val type: InteractionType,
        override val name: String,
        override val group: InteractionGroup,
        override val repeatConfig: InteractionRepeatConfig,
        override val isActive: Boolean,
    ) : Interaction

    data class Flees(
        override val id: String = randomUUID(),
        override val templateId: String,
        override val petId: String,
        override val type: InteractionType,
        override val name: String,
        override val group: InteractionGroup,
        override val repeatConfig: InteractionRepeatConfig,
        override val isActive: Boolean,
    ) : Interaction

    data class HealthCheck(
        override val id: String = randomUUID(),
        override val templateId: String,
        override val petId: String,
        override val type: InteractionType,
        override val name: String,
        override val group: InteractionGroup,
        override val repeatConfig: InteractionRepeatConfig,
        override val isActive: Boolean,
    ) : Interaction

}