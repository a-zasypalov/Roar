package com.gaoyun.roar.model.domain

import com.gaoyun.roar.model.domain.PetType.Companion.CAT_STRING
import com.gaoyun.roar.model.domain.PetType.Companion.DOG_STRING

enum class PetType {
    CAT, DOG;

    override fun toString(): String {
        return when (this) {
            CAT -> CAT_STRING
            DOG -> DOG_STRING
        }
    }

    companion object {
        const val CAT_STRING = "Cat"
        const val DOG_STRING = "Dog"
    }
}

internal fun String.toPetType(): PetType = when (this) {
    CAT_STRING -> PetType.CAT
    DOG_STRING -> PetType.DOG
    else -> throw IllegalArgumentException("Wrong pet type $this")
}