package com.gaoyun.roar.model.domain.interactions

data class InteractionRepeatConfig(
    val each: InteractionRepeatConfigEach = InteractionRepeatConfigEach.DAY,
    val onOrAt: String = "1"
) {
    override fun toString(): String = "${each}_${onOrAt}"
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
    return InteractionRepeatConfig(
        each = split[0].toInteractionRepeatConfigEach(),
        onOrAt = split[1]
    )
}