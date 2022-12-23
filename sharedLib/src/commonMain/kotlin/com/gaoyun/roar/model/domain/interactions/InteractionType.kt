package com.gaoyun.roar.model.domain.interactions

enum class InteractionType {
    DEWORMING,
    FLEES,
    HEALTH_CHECK,
    VACCINATION,
    NAILS,
    GROOMING,
    PILLS,
    BATHING,
    CUSTOM;

    companion object {
        const val DEWORMING_STRING = "deworming"
        const val FLEES_STRING = "flees"
        const val HEALTH_CHECK_STRING = "health_check"
        const val VACCINATION_STRING = "vaccination"
        const val NAILS_STRING = "nails"
        const val GROOMING_STRING = "grooming"
        const val PILLS_STRING = "pills"
        const val BATHING_STRING = "bath"
        const val CUSTOM_STRING = "custom"
    }

    override fun toString(): String {
        return when (this) {
            DEWORMING -> DEWORMING_STRING
            FLEES -> FLEES_STRING
            HEALTH_CHECK -> HEALTH_CHECK_STRING
            VACCINATION -> VACCINATION_STRING
            NAILS -> NAILS_STRING
            GROOMING -> GROOMING_STRING
            PILLS -> PILLS_STRING
            BATHING -> BATHING_STRING
            CUSTOM -> CUSTOM_STRING
        }
    }
}

fun String.toInteractionType(): InteractionType {
    return when (this) {
        InteractionType.DEWORMING_STRING -> InteractionType.DEWORMING
        InteractionType.FLEES_STRING -> InteractionType.FLEES
        InteractionType.HEALTH_CHECK_STRING -> InteractionType.HEALTH_CHECK
        InteractionType.CUSTOM_STRING -> InteractionType.CUSTOM
        InteractionType.BATHING_STRING -> InteractionType.BATHING
        InteractionType.PILLS_STRING -> InteractionType.PILLS
        InteractionType.GROOMING_STRING -> InteractionType.GROOMING
        InteractionType.NAILS_STRING -> InteractionType.NAILS
        InteractionType.VACCINATION_STRING -> InteractionType.VACCINATION
        else -> throw IllegalArgumentException("Wrong interaction type $this")
    }
}