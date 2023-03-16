package com.gaoyun.roar.model.domain.interactions

import kotlinx.serialization.Serializable

@Serializable
data class InteractionRepeatConfig(
    val repeatsEveryNumber: Int = 1,
    val repeatsEveryPeriod: InteractionRepeatConfigEach = InteractionRepeatConfigEach.DAY,
    val repeatsEveryPeriodOn: String = REPEATS_EVERY_PERIOD_ON_EMPTY,
    val ends: String = ENDS_NO,
    val active: Boolean = false,
) {
    override fun toString(): String = "${repeatsEveryNumber}_${repeatsEveryPeriod}_${repeatsEveryPeriodOn}_$ends"

    companion object {
        const val REPEATS_EVERY_PERIOD_ON_EMPTY = "-"
        const val REPEATS_EVERY_PERIOD_ON_LAST = "last"
        const val ENDS_NO = "no.0"
        const val ENDS_DATE = "date"
        const val ENDS_TIMES = "times"
    }
}

@Serializable
enum class InteractionRepeatConfigEach {
    YEAR, MONTH, WEEK, DAY;

    override fun toString(): String {
        return when (this) {
            YEAR -> YEAR_STRING
            MONTH -> MONTH_STRING
            WEEK -> WEEK_STRING
            DAY -> DAY_STRING
        }
    }

    companion object {
        const val YEAR_STRING = "year"
        const val MONTH_STRING = "month"
        const val WEEK_STRING = "week"
        const val DAY_STRING = "day"
        val LIST = listOf(
            DAY_STRING,
            WEEK_STRING,
            MONTH_STRING,
            YEAR_STRING
        )
    }
}

fun String.toInteractionRepeatConfigEach(): InteractionRepeatConfigEach {
    return when (this) {
        InteractionRepeatConfigEach.YEAR_STRING -> InteractionRepeatConfigEach.YEAR
        InteractionRepeatConfigEach.MONTH_STRING -> InteractionRepeatConfigEach.MONTH
        InteractionRepeatConfigEach.WEEK_STRING -> InteractionRepeatConfigEach.WEEK
        InteractionRepeatConfigEach.DAY_STRING -> InteractionRepeatConfigEach.DAY
        else -> throw IllegalArgumentException("Wrong repeat config each $this")
    }
}

internal fun String.toInteractionRepeatConfig(): InteractionRepeatConfig {
    val split = this.split("_")
    return if (split.size == 4) {
        InteractionRepeatConfig(
            repeatsEveryNumber = split[0].toIntOrNull() ?: 1,
            repeatsEveryPeriod = split[1].toInteractionRepeatConfigEach(),
            repeatsEveryPeriodOn = split[2],
            ends = split[3],
            active = true
        )
    } else InteractionRepeatConfig(active = false)
}