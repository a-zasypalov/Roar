package com.gaoyun.roar.ui.common.ext

import androidx.compose.runtime.Composable
import com.gaoyun.roar.model.domain.interactions.InteractionGroup
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfigEach
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.day
import roar.sharedlib.generated.resources.interaction_group_care
import roar.sharedlib.generated.resources.interaction_group_health
import roar.sharedlib.generated.resources.interaction_group_routine
import roar.sharedlib.generated.resources.last_occurrence
import roar.sharedlib.generated.resources.month
import roar.sharedlib.generated.resources.no_repeat
import roar.sharedlib.generated.resources.week
import roar.sharedlib.generated.resources.year

@OptIn(ExperimentalResourceApi::class)
@Composable
fun InteractionWithReminders.repeatConfigTextShort() = if (isActive) {
    repeatConfig?.repeatConfigTextShort() ?: stringResource(resource = Res.string.no_repeat)
} else {
    stringResource(resource = Res.string.last_occurrence)
}

@OptIn(ExperimentalResourceApi::class)
fun InteractionGroup.toLocalizedStringId() = when (this) {
    InteractionGroup.HEALTH -> Res.string.interaction_group_health
    InteractionGroup.CARE -> Res.string.interaction_group_care
    InteractionGroup.ROUTINE -> Res.string.interaction_group_routine
}

@OptIn(ExperimentalResourceApi::class)
fun InteractionRepeatConfigEach.toLocalizedStringId() = when (this) {
    InteractionRepeatConfigEach.YEAR -> Res.string.year
    InteractionRepeatConfigEach.MONTH -> Res.string.month
    InteractionRepeatConfigEach.WEEK -> Res.string.week
    InteractionRepeatConfigEach.DAY -> Res.string.day
}