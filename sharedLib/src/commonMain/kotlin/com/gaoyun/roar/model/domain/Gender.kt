package com.gaoyun.roar.model.domain

enum class Gender {
    MALE, FEMALE;

    override fun toString(): String {
        return when (this) {
            MALE -> MALE_STRING
            FEMALE -> FEMALE_STRING
        }
    }

    companion object {
        const val MALE_STRING = "male"
        const val FEMALE_STRING = "female"
    }
}

internal fun String.toGender(): Gender {
    return when (this.lowercase()) {
        Gender.MALE_STRING -> Gender.MALE
        Gender.FEMALE_STRING -> Gender.FEMALE
        else -> throw IllegalArgumentException("Wrong gender value")
    }
}