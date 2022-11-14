package com.gaoyun.roar.model.domain.interactions

enum class InteractionType {
    DEWORMING,
    FLEES,
    HEALTH_CHECK;

    companion object {
        const val DEWORMING_STRING = "deworming"
        const val FLEES_STRING = "flees"
        const val HEALTH_CHECK_STRING = "health_check"

        val TYPES_LIST = listOf(
            DEWORMING_STRING,
            FLEES_STRING,
            HEALTH_CHECK_STRING
        )
    }

    override fun toString(): String {
        return when (this) {
            DEWORMING -> DEWORMING_STRING
            FLEES -> FLEES_STRING
            HEALTH_CHECK -> HEALTH_CHECK_STRING
        }
    }
}

internal fun String.toInteractionType(): InteractionType {
    return when (this) {
        InteractionType.DEWORMING_STRING -> InteractionType.DEWORMING
        InteractionType.FLEES_STRING -> InteractionType.FLEES
        InteractionType.HEALTH_CHECK_STRING -> InteractionType.HEALTH_CHECK
        else -> throw IllegalArgumentException("Wrong interaction type $this")
    }
}