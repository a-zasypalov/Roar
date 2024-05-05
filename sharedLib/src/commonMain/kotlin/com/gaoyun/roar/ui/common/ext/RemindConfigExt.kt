package com.gaoyun.roar.ui.common.ext

import androidx.compose.runtime.Composable
import com.gaoyun.roar.model.domain.interactions.InteractionRemindConfig
import com.gaoyun.roar.model.domain.interactions.InteractionRemindConfigPeriod
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.app_name
import roar.sharedlib.generated.resources.before
import roar.sharedlib.generated.resources.day
import roar.sharedlib.generated.resources.hour
import roar.sharedlib.generated.resources.remind
import roar.sharedlib.generated.resources.week

@OptIn(ExperimentalResourceApi::class)
@Composable
fun InteractionRemindConfig.remindConfigTextFull() =
    StringBuilder().apply {
        append(stringResource(Res.string.remind))
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
        append(" ${stringResource(Res.string.before)}")
    }.toString()

@Composable
@OptIn(ExperimentalResourceApi::class)
fun InteractionRemindConfigPeriod.toLocalizedStringIdPlural() = Res.string.app_name //TODO: fix
//    when (this) {
//    InteractionRemindConfigPeriod.WEEK -> Res.string.weeks_before
//    InteractionRemindConfigPeriod.DAY -> Res.string.days_before
//    InteractionRemindConfigPeriod.HOUR -> Res.string.hours_before
//}

@Composable
@OptIn(ExperimentalResourceApi::class)
fun InteractionRemindConfigPeriod.toLocalizedStringId() = when (this) {
    InteractionRemindConfigPeriod.WEEK -> Res.string.week
    InteractionRemindConfigPeriod.DAY -> Res.string.day
    InteractionRemindConfigPeriod.HOUR -> Res.string.hour
}