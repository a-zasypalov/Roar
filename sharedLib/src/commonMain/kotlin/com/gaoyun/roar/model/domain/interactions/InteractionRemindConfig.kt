package com.gaoyun.roar.model.domain.interactions

import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

@Serializable
data class InteractionRemindConfig(
    val remindBeforeNumber: Int = 1,
    val repeatsEveryPeriod: InteractionRemindConfigPeriod = InteractionRemindConfigPeriod.HOUR,
) {
    override fun toString(): String = "${remindBeforeNumber}_${repeatsEveryPeriod}"

    fun toDuration(): Duration {
        return when (repeatsEveryPeriod) {
            InteractionRemindConfigPeriod.WEEK -> (remindBeforeNumber * 7).days
            InteractionRemindConfigPeriod.DAY -> remindBeforeNumber.days
            InteractionRemindConfigPeriod.HOUR -> remindBeforeNumber.hours
        }
    }
}

@Serializable
enum class InteractionRemindConfigPeriod {
    WEEK, DAY, HOUR;

    override fun toString(): String {
        return when (this) {
            WEEK -> WEEK_STRING
            DAY -> DAY_STRING
            HOUR -> HOUR_STRING
        }
    }

    companion object {
        const val WEEK_STRING = "week"
        const val DAY_STRING = "day"
        const val HOUR_STRING = "hour"
        val LIST = listOf(
            DAY_STRING,
            WEEK_STRING,
            HOUR_STRING,
        )
    }
}

fun String.toInteractionRemindConfigPeriod(): InteractionRemindConfigPeriod {
    return when (this) {
        InteractionRemindConfigPeriod.WEEK_STRING -> InteractionRemindConfigPeriod.WEEK
        InteractionRemindConfigPeriod.DAY_STRING -> InteractionRemindConfigPeriod.DAY
        InteractionRemindConfigPeriod.HOUR_STRING -> InteractionRemindConfigPeriod.HOUR
        else -> throw IllegalArgumentException("Wrong repeat config each $this")
    }
}

internal fun String.toInteractionRemindConfig(): InteractionRemindConfig {
    val split = this.split("_")
    return if (split.size == 2) {
        InteractionRemindConfig(
            remindBeforeNumber = split[0].toIntOrNull() ?: 1,
            repeatsEveryPeriod = split[1].toInteractionRemindConfigPeriod(),
        )
    } else InteractionRemindConfig()
}