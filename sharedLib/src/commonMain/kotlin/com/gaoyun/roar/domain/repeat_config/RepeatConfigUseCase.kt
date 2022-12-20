package com.gaoyun.roar.domain.repeat_config

import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfig
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfigEach
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.util.toLocalDate
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RepeatConfigUseCase : KoinComponent {

    private val getInteraction: GetInteraction by inject()

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
        from: LocalDate = Clock.System.now().toLocalDate()
    ): LocalDate? {

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
                "date" -> {
                    endsDate = LocalDate.parse(split[1])
                    if (endsDate <= Clock.System.now().toLocalDate()) {
                        return null
                    }
                }
                "times" -> {
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
                val betweenDays = from.dayOfWeek.isoDayNumber - daysOfWeek.min().isoDayNumber
                val result = from.plus(repeatConfig.repeatsEveryNumber, unitToAdd).minus(betweenDays, DateTimeUnit.DAY)
                checkResult(endsDate, result)
            }
        }

        if (repeatConfig.repeatsEveryPeriod == InteractionRepeatConfigEach.MONTH) {
            return if (repeatConfig.repeatsEveryPeriodOn.lowercase() == "last") {
                val nextMonthSameDate = from.plus(repeatConfig.repeatsEveryNumber, unitToAdd)
                val nextNextMonthSameDate = nextMonthSameDate.plus(1, DateTimeUnit.MONTH)
                val numberOfDaysInNextMonth = nextMonthSameDate.daysUntil(nextNextMonthSameDate)
                val result = LocalDate(year = nextMonthSameDate.year, month = nextMonthSameDate.month, dayOfMonth = numberOfDaysInNextMonth)

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
        return if (endsDate == null) result else if (result <= endsDate) result else null
    }
}