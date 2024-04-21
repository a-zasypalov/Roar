package com.gaoyun.roar.ui.common.ext

import androidx.compose.runtime.Composable
import com.gaoyun.roar.model.domain.interactions.InteractionRemindConfig
import com.gaoyun.roar.model.domain.interactions.InteractionRemindConfigPeriod

@Composable
fun InteractionRemindConfig.remindConfigTextFull() =
    StringBuilder().apply {
//        append(stringResource(id = R.string.remind))
        append(" $remindBeforeNumber ")
//        append(
//            pluralStringResource(
//                id = when (repeatsEveryPeriod) {
//                    InteractionRemindConfigPeriod.WEEK -> R.plurals.weeks
//                    InteractionRemindConfigPeriod.DAY -> R.plurals.days
//                    InteractionRemindConfigPeriod.HOUR -> R.plurals.hours
//                }, count = remindBeforeNumber
//            )
//        )
//        append(" ${stringResource(id = R.string.before)}")
    }.toString()


fun InteractionRemindConfigPeriod.toLocalizedStringIdPlural() = 1
//    when (this) {
//    InteractionRemindConfigPeriod.WEEK -> R.plurals.weeks_before
//    InteractionRemindConfigPeriod.DAY -> R.plurals.days_before
//    InteractionRemindConfigPeriod.HOUR -> R.plurals.hours_before
//}

fun InteractionRemindConfigPeriod.toLocalizedStringId() = 1
//    when (this) {
//    InteractionRemindConfigPeriod.WEEK -> R.string.week
//    InteractionRemindConfigPeriod.DAY -> R.string.day
//    InteractionRemindConfigPeriod.HOUR -> R.string.hour
//}