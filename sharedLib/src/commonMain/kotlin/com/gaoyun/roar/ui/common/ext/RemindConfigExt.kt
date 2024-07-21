package com.gaoyun.roar.ui.common.ext

import androidx.compose.runtime.Composable
import com.gaoyun.roar.model.domain.interactions.InteractionRemindConfig
import com.gaoyun.roar.model.domain.interactions.InteractionRemindConfigPeriod
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.before
import roar.sharedlib.generated.resources.day
import roar.sharedlib.generated.resources.days
import roar.sharedlib.generated.resources.days_before
import roar.sharedlib.generated.resources.hour
import roar.sharedlib.generated.resources.hours
import roar.sharedlib.generated.resources.hours_before
import roar.sharedlib.generated.resources.remind
import roar.sharedlib.generated.resources.week
import roar.sharedlib.generated.resources.weeks
import roar.sharedlib.generated.resources.weeks_before

@Composable
fun InteractionRemindConfig.remindConfigTextFull() =
    StringBuilder().apply {
        append(stringResource(Res.string.remind))
        append(" $remindBeforeNumber ")
        append(
            pluralStringResource(
                resource = when (repeatsEveryPeriod) {
                    InteractionRemindConfigPeriod.WEEK -> Res.plurals.weeks
                    InteractionRemindConfigPeriod.DAY -> Res.plurals.days
                    InteractionRemindConfigPeriod.HOUR -> Res.plurals.hours
                }, quantity = remindBeforeNumber
            )
        )
        append(" ${stringResource(Res.string.before)}")
    }.toString()

@Composable
fun InteractionRemindConfigPeriod.toLocalizedStringIdPlural() = when (this) {
    InteractionRemindConfigPeriod.WEEK -> Res.plurals.weeks_before
    InteractionRemindConfigPeriod.DAY -> Res.plurals.days_before
    InteractionRemindConfigPeriod.HOUR -> Res.plurals.hours_before
}

@Composable
fun InteractionRemindConfigPeriod.toLocalizedStringId() = when (this) {
    InteractionRemindConfigPeriod.WEEK -> Res.string.week
    InteractionRemindConfigPeriod.DAY -> Res.string.day
    InteractionRemindConfigPeriod.HOUR -> Res.string.hour
}