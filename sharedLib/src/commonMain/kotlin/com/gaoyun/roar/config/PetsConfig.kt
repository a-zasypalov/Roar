package com.gaoyun.roar.config

import com.gaoyun.roar.model.domain.PetType
import com.gaoyun.roar.model.domain.toPetType

object PetsConfig {

    val petTypes = listOf(
        PetTypeConfig(
            nameRes = "Cat",
            iconRes = "ic_cat",
            enumType = PetType.CAT
        ),
        PetTypeConfig(
            nameRes = "Dog",
            iconRes = "ic_dog",
            enumType = PetType.DOG
        )
    )

    fun petAvatars(petType: String) = (1..50).map { PetAvatarConfig("ic_${petType.toPetType()}_$it") }

    data class PetTypeConfig(
        val nameRes: String,
        val iconRes: String,
        val enumType: PetType
    )

    data class PetAvatarConfig(
        val iconRes: String,
    )

}