package com.gaoyun.common.ext

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.gaoyun.common.R
import com.gaoyun.roar.model.domain.interactions.InteractionGroup
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfigEach
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders

@Composable
fun InteractionWithReminders.repeatConfigTextShort() = if (isActive) {
    repeatConfig?.repeatConfigTextShort() ?: stringResource(id = R.string.no_repeat)
} else {
    stringResource(id = R.string.last_occurrence)
}

fun InteractionGroup.toLocalizedStringId() = when (this) {
    InteractionGroup.HEALTH -> R.string.interaction_group_health
    InteractionGroup.CARE -> R.string.interaction_group_care
    InteractionGroup.ROUTINE -> R.string.interaction_group_routine
}

fun InteractionRepeatConfigEach.toLocalizedStringId() = when (this) {
    InteractionRepeatConfigEach.YEAR -> R.string.year
    InteractionRepeatConfigEach.MONTH -> R.string.month
    InteractionRepeatConfigEach.WEEK -> R.string.week
    InteractionRepeatConfigEach.DAY -> R.string.day
}