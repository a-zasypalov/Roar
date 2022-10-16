package com.gaoyun.roar.model.domain

import com.gaoyun.roar.model.entity.UserEntity
import com.gaoyun.roar.util.randomUUID

data class User(
    val id: String = randomUUID(),
    val name: String
)

fun UserEntity.toDomain(): User = User(id = id, name = name)