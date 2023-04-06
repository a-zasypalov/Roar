package com.gaoyun.roar.model.domain

import com.gaoyun.roar.model.entity.UserEntity
import com.gaoyun.roar.util.randomUUID
import kotlinx.datetime.Clock
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
    val pets: List<PetWithInteractions>,
    val timestamp: Long = Clock.System.now().toEpochMilliseconds(),
)

fun UserEntity.toDomain(): User = User(id = id, name = name)

fun User.withPets(pets: List<PetWithInteractions>) = UserWithPets(
    id = id,
    name = name,
    pets = pets,
    timestamp = Clock.System.now().toEpochMilliseconds(),
)

fun UserWithPets.withoutPets() = User(
    id = id,
    name = name,
)