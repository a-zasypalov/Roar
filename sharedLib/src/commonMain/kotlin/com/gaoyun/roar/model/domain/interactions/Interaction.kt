package com.gaoyun.roar.model.domain.interactions

import com.gaoyun.roar.model.domain.Reminder
import com.gaoyun.roar.model.entity.InteractionEntity
import com.gaoyun.roar.util.randomUUID
import kotlinx.serialization.Serializable

@Serializable
data class Interaction(
    val id: String = randomUUID(),
    val templateId: String? = null,
    val petId: String,
    val type: InteractionType,
    val name: String,
    val group: InteractionGroup,
    val repeatConfig: InteractionRepeatConfig? = null,
    val remindConfig: InteractionRemindConfig? = null,
    val isActive: Boolean,
    val notes: String = ""
)

@Serializable
data class InteractionWithReminders(
    val id: String = randomUUID(),
    val templateId: String? = null,
    val petId: String,
    val type: InteractionType,
    val name: String,
    val group: InteractionGroup,
    val repeatConfig: InteractionRepeatConfig? = null,
    val remindConfig: InteractionRemindConfig? = null,
    val isActive: Boolean,
    val notes: String = "",
    val reminders: List<Reminder> = listOf()
)

internal fun Interaction.withReminders(reminders: List<Reminder>): InteractionWithReminders {
    return InteractionWithReminders(
        id = id,
        templateId = templateId,
        petId = petId,
        type = type,
        name = name,
        group = group,
        repeatConfig = repeatConfig,
        remindConfig = remindConfig,
        isActive = isActive,
        notes = notes,
        reminders = reminders
    )
}

internal fun InteractionWithReminders.withoutReminders(): Interaction {
    return Interaction(
        id = id,
        templateId = templateId,
        petId = petId,
        type = type,
        name = name,
        group = group,
        repeatConfig = repeatConfig,
        remindConfig = remindConfig,
        isActive = isActive,
        notes = notes,
    )
}

internal fun InteractionEntity.toDomain(): Interaction {
    return Interaction(
        id = id,
        templateId = templateId,
        petId = petId,
        type = type.toInteractionType(),
        name = name,
        group = interactionGroup.toInteractionGroup(),
        repeatConfig = repeatConfig?.toInteractionRepeatConfig(),
        remindConfig = remindConfig?.toInteractionRemindConfig(),
        isActive = isActive,
        notes = notes
    )
}