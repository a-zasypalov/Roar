package com.gaoyun.common.ext

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.decapitalize
import androidx.compose.ui.text.intl.Locale
import com.gaoyun.common.DateUtils
import com.gaoyun.common.R
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfig
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfigEach
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate

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
            when (repeatsEveryPeriod) {
                InteractionRepeatConfigEach.MONTH -> {
                    append(" ")
                    append(stringResource(id = R.string.date_on))
                    append(" ")
                    repeatsEveryPeriodOn.toIntOrNull()?.let { dayNumber ->
                        append("$dayNumber ${stringResource(id = R.string.day)}")
                    } ?: if (repeatsEveryPeriodOn == InteractionRepeatConfig.REPEATS_EVERY_PERIOD_ON_LAST) {
                        append(stringResource(id = R.string.the_last_day))
                    } else if (repeatsEveryPeriodOn == InteractionRepeatConfig.REPEATS_EVERY_PERIOD_ON_SAME) {
                        append(stringResource(id = R.string.the_same_day))
                    }
                }
                InteractionRepeatConfigEach.WEEK -> {
                    append(" ")
                    append(stringResource(id = R.string.date_on))
                    append(" ")
                    append(repeatsEveryPeriodOn.split(",")
                        .mapNotNull { it.toIntOrNull() }
                        .map { stringArrayResource(id = R.array.weekdays_short)[it - 1] }
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