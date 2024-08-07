package com.gaoyun.roar.ui.features.interactions

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.model.domain.Reminder
import com.gaoyun.roar.presentation.interactions.InteractionScreenContract
import com.gaoyun.roar.ui.common.composables.LabelledCheckBox
import com.gaoyun.roar.ui.theme.RoarTheme
import com.gaoyun.roar.util.DateFormats
import com.gaoyun.roar.util.SharedDateUtils
import com.gaoyun.roar.util.formatDate
import com.gaoyun.roar.util.formatDateTime
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.at

@Composable
fun ReminderItems(
    reminders: List<Reminder>,
    onReminderCompleteClick: (InteractionScreenContract.Event.OnReminderCompleteClick) -> Unit,
    onCompleteReminderNotTodayClick: (InteractionScreenContract.Event.OnCompleteReminderNotTodayClick) -> Unit
) {
    ReminderCard {
        Column {
            reminders.map { reminder ->
                ReminderItem(
                    reminder = reminder,
                    onCheckedChange = { isComplete ->
                        if (reminder.dateTime.date == SharedDateUtils.currentDate()) {
                            onReminderCompleteClick(
                                InteractionScreenContract.Event.OnReminderCompleteClick(
                                    reminderId = reminder.id,
                                    isComplete = isComplete,
                                    completionDateTime = reminder.dateTime
                                )
                            )
                        } else {
                            onCompleteReminderNotTodayClick(
                                InteractionScreenContract.Event.OnCompleteReminderNotTodayClick(
                                    reminder.id,
                                    reminder.dateTime
                                )
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun CompleteReminderItems(
    reminders: List<Reminder>,
    onReminderCompleteClick: (InteractionScreenContract.Event.OnReminderCompleteClick) -> Unit,
    onReminderRemoveFromHistoryClick: (InteractionScreenContract.Event.OnReminderRemoveFromHistoryClick) -> Unit
) {
    ReminderCard {
        Column(Modifier.animateContentSize()) {
            reminders.mapIndexed { index, reminder ->
                ReminderItem(
                    reminder = reminder,
                    onCheckedChange = { isComplete ->
                        if (index == 0) {
                            onReminderCompleteClick(
                                InteractionScreenContract.Event.OnReminderCompleteClick(
                                    reminderId = reminder.id,
                                    isComplete = isComplete,
                                    completionDateTime = reminder.dateTime
                                )
                            )
                        } else {
                            onReminderRemoveFromHistoryClick(
                                InteractionScreenContract.Event.OnReminderRemoveFromHistoryClick(
                                    reminder.id
                                )
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun ReminderCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        tonalElevation = RoarTheme.INTERACTION_CARD_ELEVATION,
        shape = MaterialTheme.shapes.large,
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        content = content
    )
}

@Composable
private fun ReminderItem(
    reminder: Reminder,
    onCheckedChange: (Boolean) -> Unit
) {
    LabelledCheckBox(
        checked = reminder.isCompleted,
        label = "${
            if (reminder.dateTime.date.year != SharedDateUtils.currentYear()) {
                reminder.dateTime.date.formatDate(DateFormats.DD_MMMM_YYYY_DATE_FORMAT)
            } else {
                reminder.dateTime.date.formatDate(DateFormats.DD_MMMM_DATE_FORMAT)
            }
        } ${stringResource(resource = Res.string.at)} ${
            reminder.dateTime.formatDateTime(DateFormats.HH_MM_TIME_FORMAT)
        }",
        modifier = Modifier.fillMaxWidth(),
        verticalPadding = 16.dp,
        horizontalPadding = 20.dp,
        spacerSize = 14.dp,
        onCheckedChange = onCheckedChange
    )
}