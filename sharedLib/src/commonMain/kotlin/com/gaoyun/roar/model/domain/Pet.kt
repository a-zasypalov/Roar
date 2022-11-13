package com.gaoyun.roar.model.domain

import com.gaoyun.roar.model.entity.PetEntity
import com.gaoyun.roar.util.randomUUID
import kotlinx.datetime.LocalDate


data class Pet(
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
)

fun PetEntity.toDomain() = Pet(
    id = id,
    petType = petType.toPetType(),
    breed = breed,
    name = name,
    avatar = avatar,
    userId = userId,
    birthday = LocalDate.parse(birthday),
    isSterilized = isSterilized,
    gender = gender.toGender(),
    chipNumber = chipNumber,
    dateCreated = LocalDate.parse(dateCreated),
)


