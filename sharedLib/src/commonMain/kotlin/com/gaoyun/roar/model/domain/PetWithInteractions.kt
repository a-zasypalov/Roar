package com.gaoyun.roar.model.domain

import com.gaoyun.roar.model.domain.interactions.InteractionGroup
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.util.randomUUID
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class PetWithInteractions(
    val id: String = randomUUID(),
    val petType: PetType,
    val breed: String,
    val name: String,
    val avatar: String,
    val userId: String,
    val birthday: LocalDate,
    val isSterilized: Boolean,
    val gender: Gender,
    val chipNumber: String,
    val dateCreated: LocalDate,
    val interactions: Map<InteractionGroup, List<InteractionWithReminders>>
) {
    companion object {
        fun preview() = Pet.preview().withInteractions(mapOf(InteractionGroup.CARE to listOf(InteractionWithReminders.preview())))
    }
}

fun Pet.withInteractions(interactions: Map<InteractionGroup, List<InteractionWithReminders>>) = PetWithInteractions(
    id = id,
    petType = petType,
    breed = breed,
    name = name,
    avatar = avatar,
    userId = userId,
    birthday = birthday,
    isSterilized = isSterilized,
    gender = gender,
    chipNumber = chipNumber,
    dateCreated = dateCreated,
    interactions = interactions,
)

fun Pet.withInteractions(interactions: List<InteractionWithReminders>?) = PetWithInteractions(
    id = id,
    petType = petType,
    breed = breed,
    name = name,
    avatar = avatar,
    userId = userId,
    birthday = birthday,
    isSterilized = isSterilized,
    gender = gender,
    chipNumber = chipNumber,
    dateCreated = dateCreated,
    interactions = interactions?.groupBy { it.group } ?: emptyMap(),
)

fun PetWithInteractions.withoutInteractions() = Pet(
    id = id,
    petType = petType,
    breed = breed,
    name = name,
    avatar = avatar,
    userId = userId,
    birthday = birthday,
    isSterilized = isSterilized,
    gender = gender,
    chipNumber = chipNumber,
    dateCreated = dateCreated,
)

