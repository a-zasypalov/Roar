package com.gaoyun.roar.model.domain.interactions

import kotlinx.serialization.Serializable

@Serializable
enum class InteractionGroup {
    HEALTH,
    CARE,
    ROUTINE;

    override fun toString(): String {
        return when (this) {
            CARE -> CARE_STRING
            HEALTH -> HEALTH_STRING
            ROUTINE -> ROUTINE_STRING
        }
    }

    companion object {
        const val HEALTH_STRING = "health"
        const val CARE_STRING = "care"
        const val ROUTINE_STRING = "routine"

        val GROUP_LIST = listOf(
            HEALTH,
            CARE,
            ROUTINE
        )
    }
}

fun String.toInteractionGroup(): InteractionGroup {
    return when (this) {
        InteractionGroup.HEALTH_STRING -> InteractionGroup.HEALTH
        InteractionGroup.CARE_STRING -> InteractionGroup.CARE
        InteractionGroup.ROUTINE_STRING -> InteractionGroup.ROUTINE
        else -> throw IllegalArgumentException("Wrong group type $this")
    }
}