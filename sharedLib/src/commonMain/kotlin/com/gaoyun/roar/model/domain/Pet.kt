package com.gaoyun.roar.model.domain

import com.gaoyun.roar.model.entity.PetEntity
import com.gaoyun.roar.util.randomUUID
import kotlinx.datetime.LocalDate

data class Pet(
    val id: String = randomUUID(),
    val petType: String,
    val breed: String,
    val name: String,
    val userId: String,
    val birthday: LocalDate,
    val isSterilized: Boolean,
    val dateCreated: LocalDate,
    val reminders: List<String> = listOf()
)

fun PetEntity.toDomain() = Pet(
    id = id,
    petType = petType,
    breed = breed,
    name = name,
    userId = userId,
    birthday = LocalDate.parse(birthday),
    isSterilized = isSterilized,
    dateCreated = LocalDate.parse(dateCreated),
    reminders = reminders ?: emptyList()
)
