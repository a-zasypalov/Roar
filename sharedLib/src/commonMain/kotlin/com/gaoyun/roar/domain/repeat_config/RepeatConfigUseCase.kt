package com.gaoyun.roar.domain.repeat_config

import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfig
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfig.Companion.ENDS_DATE
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfig.Companion.ENDS_TIMES
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfig.Companion.REPEATS_EVERY_PERIOD_ON_LAST
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfig.Companion.REPEATS_EVERY_PERIOD_ON_SAME
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfigEach
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.util.toLocalDate
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus

class RepeatConfigUseCase(
    private val getInteraction: GetInteraction,
) {

    suspend fun getNextDateAccordingToRepeatConfig(
        interactionId: String,
        repeatConfig: InteractionRepeatConfig,
        from: LocalDate = Clock.System.now().toLocalDate()
    ): LocalDate? =
        getInteraction.getInteractionWithReminders(interactionId).firstOrNull()?.let {
            getNextDateAccordingToRepeatConfig(repeatConfig = repeatConfig, from = from, interaction = it)
        }

    private fun getNextDateAccordingToRepeatConfig(
        repeatConfig: InteractionRepeatConfig,
        interaction: InteractionWithReminders,
        from: LocalDate,
    ): LocalDate? {
        println("From date: $from")
        val unitToAdd = when (repeatConfig.repeatsEveryPeriod) {
            InteractionRepeatConfigEach.YEAR -> DateTimeUnit.YEAR
            InteractionRepeatConfigEach.MONTH -> DateTimeUnit.MONTH
            InteractionRepeatConfigEach.WEEK -> DateTimeUnit.WEEK
            InteractionRepeatConfigEach.DAY -> DateTimeUnit.DAY
        }

        var endsDate: LocalDate? = null

        if (!repeatConfig.ends.contains("no")) {
            val split = repeatConfig.ends.split(".")
            when (split[0]) {
                ENDS_DATE -> {
                    endsDate = LocalDate.parse(split[1])
                    if (endsDate <= Clock.System.now().toLocalDate()) {
                        return null
                    }
                }

                ENDS_TIMES -> {
                    if ((split[1].toIntOrNull() ?: 0) <= interaction.reminders.size) return null
                }
            }
        }

        if (repeatConfig.repeatsEveryPeriod == InteractionRepeatConfigEach.YEAR || repeatConfig.repeatsEveryPeriod == InteractionRepeatConfigEach.DAY) {
            val result = from.plus(repeatConfig.repeatsEveryNumber, unitToAdd)
            return checkResult(endsDate, result)
        }

        if (repeatConfig.repeatsEveryPeriod == InteractionRepeatConfigEach.WEEK) {
            val daysOfWeek = repeatConfig.repeatsEveryPeriodOn.split(",")
                .mapNotNull { it.toIntOrNull() }
                .sorted()
                .map { DayOfWeek(it) }

            val nextOccasionOnThisWeek = daysOfWeek.firstOrNull { it > from.dayOfWeek }

            return if (nextOccasionOnThisWeek != null) {
                val betweenDays = nextOccasionOnThisWeek.isoDayNumber - from.dayOfWeek.isoDayNumber
                val result = from.plus(betweenDays, DateTimeUnit.DAY)
                checkResult(endsDate, result)
            } else {
                val betweenDays = from.dayOfWeek.isoDayNumber - (daysOfWeek.minOrNull()?.isoDayNumber ?: from.dayOfWeek.isoDayNumber)
                val result = from.plus(repeatConfig.repeatsEveryNumber, unitToAdd).minus(betweenDays, DateTimeUnit.DAY)
                checkResult(endsDate, result)
            }
        }

        if (repeatConfig.repeatsEveryPeriod == InteractionRepeatConfigEach.MONTH) {
            return if (repeatConfig.repeatsEveryPeriodOn.lowercase() == REPEATS_EVERY_PERIOD_ON_LAST) {
                val nextMonthSameDate = from.plus(repeatConfig.repeatsEveryNumber, unitToAdd)
                val nextNextMonthSameDate = nextMonthSameDate.plus(1, DateTimeUnit.MONTH)
                val numberOfDaysInNextMonth = nextMonthSameDate.daysUntil(nextNextMonthSameDate)
                val result = LocalDate(year = nextMonthSameDate.year, month = nextMonthSameDate.month, dayOfMonth = numberOfDaysInNextMonth)

                checkResult(endsDate, result)
            } else if (repeatConfig.repeatsEveryPeriodOn.lowercase() == REPEATS_EVERY_PERIOD_ON_SAME) {
                val nextMonthSameDate = from.plus(repeatConfig.repeatsEveryNumber, unitToAdd)
                val result = LocalDate(year = nextMonthSameDate.year, month = nextMonthSameDate.month, dayOfMonth = nextMonthSameDate.dayOfMonth)

                checkResult(endsDate, result)
            } else {
                val day = repeatConfig.repeatsEveryPeriodOn.toIntOrNull() ?: 1
                val nextMonthSameDate = from.plus(repeatConfig.repeatsEveryNumber, unitToAdd)
                val result = LocalDate(year = nextMonthSameDate.year, month = nextMonthSameDate.month, dayOfMonth = day)

                checkResult(endsDate, result)
            }
        }

        return null

    }

    private fun checkResult(endsDate: LocalDate?, result: LocalDate): LocalDate? {
        println("Result: $result")
        return if (endsDate == null) result else if (result <= endsDate) result else null
    }
}