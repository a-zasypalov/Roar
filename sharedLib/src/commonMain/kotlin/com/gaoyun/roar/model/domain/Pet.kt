package com.gaoyun.roar.model.domain

import com.gaoyun.roar.model.entity.PetEntity
import com.gaoyun.roar.util.randomUUID
import com.gaoyun.roar.util.toLocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
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
) {
    companion object {
        fun preview() = Pet(
            petType = PetType.CAT,
            breed = "Colorpoint Shorthair",
            name = "Senior Android Developer",
            avatar = "ic_cat_15",
            userId = "123",
            birthday = Clock.System.now().toLocalDate(),
            isSterilized = false,
            gender = Gender.MALE,
            chipNumber = "123123456456",
            dateCreated = Clock.System.now().toLocalDate()
        )
    }
}

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


