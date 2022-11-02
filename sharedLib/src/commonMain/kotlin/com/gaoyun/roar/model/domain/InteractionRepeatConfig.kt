package com.gaoyun.roar.model.domain

data class InteractionRepeatConfig(
    val each: InteractionRepeatConfigEach,
    val onOrAt: String
)

enum class InteractionRepeatConfigEach {
    YEAR, MONTH, WEEK, DAY;

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