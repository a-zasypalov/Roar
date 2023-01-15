package com.gaoyun.roar.model.domain

import com.gaoyun.roar.model.entity.UserEntity
import com.gaoyun.roar.util.randomUUID
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String = randomUUID(),
    val name: String
)


@Serializable
data class UserWithPets(
    val id: String = randomUUID(),
    val name: String,
    val pets: List<PetWithInteractions>
)

fun UserEntity.toDomain(): User = User(id = id, name = name)

fun User.withPets(pets: List<PetWithInteractions>) = UserWithPets(
    id = id,
    name = name,
    pets = pets
)

fun UserWithPets.withoutPets() = User(
    id = id,
    name = name,
)