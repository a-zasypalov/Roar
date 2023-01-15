package com.gaoyun.roar.model.domain

import com.gaoyun.roar.model.domain.PetType.Companion.CAT_STRING
import com.gaoyun.roar.model.domain.PetType.Companion.DOG_STRING
import kotlinx.serialization.Serializable

@Serializable
enum class PetType {
    CAT, DOG;

    override fun toString(): String {
        return when (this) {
            CAT -> CAT_STRING
            DOG -> DOG_STRING
        }
    }

    companion object {
        const val CAT_STRING = "cat"
        const val DOG_STRING = "dog"
    }
}

internal fun String.toPetType(): PetType = when (this.lowercase()) {
    CAT_STRING -> PetType.CAT
    DOG_STRING -> PetType.DOG
    else -> throw IllegalArgumentException("Wrong pet type $this")
}