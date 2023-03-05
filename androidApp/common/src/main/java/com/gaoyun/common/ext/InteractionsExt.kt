@file:OptIn(ExperimentalComposeUiApi::class)

package com.gaoyun.common.ext

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.decapitalize
import androidx.compose.ui.text.intl.Locale
import com.gaoyun.common.DateUtils
import com.gaoyun.common.R
import com.gaoyun.roar.model.domain.interactions.InteractionGroup
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfig
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfigEach
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate


@Composable
fun InteractionWithReminders.repeatConfigTextShort() = if (isActive) {
    repeatConfig?.repeatConfigTextShort() ?: stringResource(id = R.string.no_repeat)
} else {
    stringResource(id = R.string.last_occurrence)
}

@Composable
fun InteractionRepeatConfig.repeatConfigTextShort() = StringBuilder().apply {
    append(stringResource(id = R.string.repeats_every))
    append(" ")
    if (repeatsEveryNumber > 1) {
        append("$repeatsEveryNumber ")
    }
    append(
        pluralStringResource(
            id = when (repeatsEveryPeriod) {
                InteractionRepeatConfigEach.YEAR -> R.plurals.years
                InteractionRepeatConfigEach.MONTH -> R.plurals.months
                InteractionRepeatConfigEach.WEEK -> R.plurals.weeks
                InteractionRepeatConfigEach.DAY -> R.plurals.days
            }, count = repeatsEveryNumber
        )
    )
    append(" ")
}.toString()

@Composable
fun InteractionRepeatConfig.repeatConfigTextFull() =
    StringBuilder().apply {
        append(stringResource(id = R.string.repeats_every))
        append(" ")
        if (repeatsEveryNumber > 1) {
            append("$repeatsEveryNumber ")
        }
        append(
            pluralStringResource(
                id = when (repeatsEveryPeriod) {
                    InteractionRepeatConfigEach.YEAR -> R.plurals.years
                    InteractionRepeatConfigEach.MONTH -> R.plurals.months
                    InteractionRepeatConfigEach.WEEK -> R.plurals.weeks
                    InteractionRepeatConfigEach.DAY -> R.plurals.days
                }, count = repeatsEveryNumber
            )
        )
        if (repeatsEveryPeriodOn != InteractionRepeatConfig.REPEATS_EVERY_PERIOD_ON_EMPTY) {
            append(" ")
            when (repeatsEveryPeriod) {
                InteractionRepeatConfigEach.MONTH -> {
                    append(stringResource(id = R.string.date_on))
                    append(" ")
                    repeatsEveryPeriodOn.split(" ").getOrNull(1)?.toIntOrNull()?.let { dayNumber ->
                        append("$dayNumber ${stringResource(id = R.string.day)}")
                    } ?: append(stringResource(id = R.string.last_day).decapitalize(Locale.current))
                }
                InteractionRepeatConfigEach.WEEK -> {
                    append(stringResource(id = R.string.date_on))
                    append(" ")
                    append(repeatsEveryPeriodOn.split(",")
                        .mapNotNull { it.toIntOrNull() }
                        .map { stringArrayResource(id = R.array.weekdays_short)[it-1] }
                        .joinToString(", ")
                    )
                }
                else -> {}
            }
        }
        if (ends == InteractionRepeatConfig.ENDS_NO) {
            append(", ")
            append(stringResource(id = R.string.never_ends))
        } else {
            val endsSplit = ends.split(".")
            when (endsSplit[0]) {
                InteractionRepeatConfig.ENDS_DATE -> {
                    append(", ")
                    append(stringResource(id = R.string.ends).decapitalize(Locale.current))
                    append(" ")
                    val date = LocalDate.parse(endsSplit[1])
                    append(date.toJavaLocalDate().format(DateUtils.ddMmmYyyyDateFormatter))
                }
                InteractionRepeatConfig.ENDS_TIMES -> {
                    append(" (${endsSplit[1]} ${pluralStringResource(id = R.plurals.times, count = endsSplit[1].toInt())})")
                }
            }
        }
    }.toString()

fun InteractionGroup.toLocalizedStringId() = when (this) {
    InteractionGroup.HEALTH -> R.string.interaction_group_health
    InteractionGroup.CARE -> R.string.interaction_group_care
    InteractionGroup.ROUTINE -> R.string.interaction_group_routine
}

fun InteractionRepeatConfigEach.toLocalizedStringId() = when(this) {
    InteractionRepeatConfigEach.YEAR -> R.string.year
    InteractionRepeatConfigEach.MONTH -> R.string.month
    InteractionRepeatConfigEach.WEEK -> R.string.week
    InteractionRepeatConfigEach.DAY -> R.string.day
}