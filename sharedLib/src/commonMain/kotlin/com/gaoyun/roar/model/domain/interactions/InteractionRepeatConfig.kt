package com.gaoyun.roar.model.domain.interactions

data class InteractionRepeatConfig(
    val repeatsEveryNumber: Int = 1,
    val repeatsEveryPeriod: InteractionRepeatConfigEach = InteractionRepeatConfigEach.DAY,
    val repeatsEveryPeriodOn: String = "-",
    val startsOn: String = "12.12.2022",
    val ends: String = "no",
    val active: Boolean = false,
) {
    override fun toString(): String = "${repeatsEveryNumber}_${repeatsEveryPeriod}_${repeatsEveryPeriodOn}_${startsOn}_$ends"
}

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
    }
}

internal fun String.toInteractionRepeatConfigEach(): InteractionRepeatConfigEach {
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
    return if (split.size == 5) {
        InteractionRepeatConfig(
            repeatsEveryNumber = split[0].toIntOrNull() ?: 1,
            repeatsEveryPeriod = split[1].toInteractionRepeatConfigEach(),
            repeatsEveryPeriodOn = split[2],
            startsOn = split[3],
            ends = split[4],
            active = true
        )
    } else InteractionRepeatConfig(active = false)
}