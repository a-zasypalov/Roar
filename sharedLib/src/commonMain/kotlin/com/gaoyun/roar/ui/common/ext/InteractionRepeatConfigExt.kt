package com.gaoyun.roar.ui.common.ext

import androidx.compose.runtime.Composable
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfig
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfigEach
import com.gaoyun.roar.util.DateFormats
import com.gaoyun.roar.util.formatDate
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.date_on
import roar.sharedlib.generated.resources.day
import roar.sharedlib.generated.resources.days
import roar.sharedlib.generated.resources.ends
import roar.sharedlib.generated.resources.months
import roar.sharedlib.generated.resources.never_ends
import roar.sharedlib.generated.resources.repeats_every
import roar.sharedlib.generated.resources.the_last_day
import roar.sharedlib.generated.resources.the_same_day
import roar.sharedlib.generated.resources.times
import roar.sharedlib.generated.resources.weekdays_short
import roar.sharedlib.generated.resources.weeks
import roar.sharedlib.generated.resources.years

@Composable
fun InteractionRepeatConfig.repeatConfigTextShort() = StringBuilder().apply {
    append(stringResource(Res.string.repeats_every))
    append(" ")
    if (repeatsEveryNumber > 1) {
        append("$repeatsEveryNumber ")
    }
    append(
        pluralStringResource(
            resource = when (repeatsEveryPeriod) {
                InteractionRepeatConfigEach.YEAR -> Res.plurals.years
                InteractionRepeatConfigEach.MONTH -> Res.plurals.months
                InteractionRepeatConfigEach.WEEK -> Res.plurals.weeks
                InteractionRepeatConfigEach.DAY -> Res.plurals.days
            }, quantity = repeatsEveryNumber
        )
    )
    append(" ")
}.toString()

@Composable
fun InteractionRepeatConfig.repeatConfigTextFull() =
    StringBuilder().apply {
        append(stringResource(Res.string.repeats_every))
        append(" ")
        if (repeatsEveryNumber > 1) {
            append("$repeatsEveryNumber ")
        }
        append(
            pluralStringResource(
                resource = when (repeatsEveryPeriod) {
                    InteractionRepeatConfigEach.YEAR -> Res.plurals.years
                    InteractionRepeatConfigEach.MONTH -> Res.plurals.months
                    InteractionRepeatConfigEach.WEEK -> Res.plurals.weeks
                    InteractionRepeatConfigEach.DAY -> Res.plurals.days
                }, quantity = repeatsEveryNumber
            )
        )
        if (repeatsEveryPeriodOn != InteractionRepeatConfig.REPEATS_EVERY_PERIOD_ON_EMPTY) {
            when (repeatsEveryPeriod) {
                InteractionRepeatConfigEach.MONTH -> {
                    append(" ")
                    append(stringResource(Res.string.date_on))
                    append(" ")
                    repeatsEveryPeriodOn.toIntOrNull()?.let { dayNumber ->
                        append("$dayNumber ${stringResource(Res.string.day)}")
                    } ?: if (repeatsEveryPeriodOn == InteractionRepeatConfig.REPEATS_EVERY_PERIOD_ON_LAST) {
                        append(stringResource(Res.string.the_last_day))
                    } else if (repeatsEveryPeriodOn == InteractionRepeatConfig.REPEATS_EVERY_PERIOD_ON_SAME) {
                        append(stringResource(Res.string.the_same_day))
                    } else {
                        append("")
                    }
                }

                InteractionRepeatConfigEach.WEEK -> {
                    append(" ")
                    append(stringResource(Res.string.date_on))
                    append(" ")
                    append(repeatsEveryPeriodOn.split(",")
                        .mapNotNull { it.toIntOrNull() }
                        .map { stringArrayResource(Res.array.weekdays_short)[it - 1] }
                        .joinToString(", ")
                    )
                }

                else -> {}
            }
        }
        if (ends == InteractionRepeatConfig.ENDS_NO) {
            append(", ")
            append(stringResource(Res.string.never_ends))
        } else {
            val endsSplit = ends.split(".")
            when (endsSplit[0]) {
                InteractionRepeatConfig.ENDS_DATE -> {
                    append(", ")
                    append(stringResource(Res.string.ends).replaceFirstChar { it.lowercase() })
                    append(" ")
                    val date = LocalDate.parse(endsSplit[1])
                    append(date.formatDate(DateFormats.ddMmmYyyyDateFormat))
                }

                InteractionRepeatConfig.ENDS_TIMES -> {
                    append(" (${endsSplit[1]} ${pluralStringResource(resource = Res.plurals.times, quantity = endsSplit[1].toInt())})")
                }
            }
        }
    }.toString()