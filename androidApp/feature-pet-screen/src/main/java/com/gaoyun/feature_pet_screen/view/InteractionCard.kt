package com.gaoyun.feature_pet_screen.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gaoyun.common.DateUtils
import com.gaoyun.common.icon
import com.gaoyun.common.ui.LabelledCheckBox
import com.gaoyun.common.ui.RoarIcon
import com.gaoyun.common.ui.Spacer
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.util.toLocalDate
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.*

@Composable
fun InteractionCard(
    interaction: InteractionWithReminders,
    showLastReminder: Boolean,
    elevation: Dp,
    shape: Shape,
    onClick: (String) -> Unit,
    onInteractionCheckClicked: (String, Boolean, java.time.LocalDateTime) -> Unit,
    modifier: Modifier = Modifier,
) {

    val reminderIdToShow = remember { mutableStateOf<String?>(null) }
    val nextReminderLabel = remember { mutableStateOf<String?>(null) }

    Surface(
        tonalElevation = elevation,
        shape = shape,
        modifier = modifier,
    ) {
        Column(modifier = Modifier
            .clickable {
                onClick(interaction.id)
                MainScope().launch {
                    delay(300)
                    reminderIdToShow.value = null
                    nextReminderLabel.value = null
                }
            }
            .padding(top = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                RoarIcon(
                    icon = interaction.type.icon(),
                    contentDescription = "reminder",
                    modifier = Modifier
                        .padding(4.dp)
                        .size(32.dp)
                )
                Spacer(size = 8.dp)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        interaction.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    val repeatText = interaction.repeatConfig?.toString() ?: "No repeat"
                    Text(
                        repeatText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(size = 8.dp)

            val reminderToShow = if (reminderIdToShow.value != null) {
                var reminder = interaction.reminders.firstOrNull { it.id == reminderIdToShow.value }
                if (reminder == null || interaction.reminders.sortedByDescending { it.dateTime }.indexOf(reminder) > 1) {
                    reminder = interaction.reminders
                        .filter { !it.isCompleted || it.dateTime > Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) }
                        .maxByOrNull { it.dateTime }
                        .also {
                            reminderIdToShow.value = it?.id
                            nextReminderLabel.value = null
                        }
                }
                reminder
            } else {
                interaction.reminders
                    .filter { !it.isCompleted || it.dateTime > Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) }
                    .maxByOrNull { it.dateTime }
                    .also { reminderIdToShow.value = it?.id }
            }

            if (reminderToShow != null) {
                LabelledCheckBox(
                    checked = reminderToShow.isCompleted,
                    label = "${
                        if (reminderToShow.dateTime.date.year != Clock.System.now().toLocalDate().year) {
                            reminderToShow.dateTime.date.toJavaLocalDate().format(DateUtils.ddMmmmYyyyDateFormatter)
                        } else {
                            reminderToShow.dateTime.date.toJavaLocalDate().format(DateUtils.ddMmmmDateFormatter)
                        }
                    } at ${
                        reminderToShow.dateTime.toJavaLocalDateTime().format(DateUtils.hhMmTimeFormatter)
                    }",
                    modifier = Modifier.fillMaxWidth(),
                    verticalPadding = 16.dp,
                    horizontalPadding = 20.dp,
                    spacerSize = 16.dp,
                    onCheckedChange = { onInteractionCheckClicked(reminderToShow.id, it, reminderToShow.dateTime.toJavaLocalDateTime()) }
                )

                interaction.reminders
                    .filter { !it.isCompleted || it.dateTime > Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) }
                    .maxByOrNull { it.dateTime }?.let { nextReminder ->
                        AnimatedVisibility(visible = showLastReminder && nextReminder.id != reminderIdToShow.value) {
                            val nextReminderText = "Next: ${
                                if (nextReminder.dateTime.date.year != Clock.System.now().toLocalDate().year) {
                                    nextReminder.dateTime.date.toJavaLocalDate().format(DateUtils.ddMmmmYyyyDateFormatter)
                                } else {
                                    nextReminder.dateTime.date.toJavaLocalDate().format(DateUtils.ddMmmmDateFormatter)
                                }
                            } at ${
                                nextReminder.dateTime.toJavaLocalDateTime().format(DateUtils.hhMmTimeFormatter)
                            }"

                            if (nextReminderLabel.value == null) nextReminderLabel.value = nextReminderText

                            Divider()

                            Text(
                                text = nextReminderLabel.value.toString(),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 22.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
                            )

                        }
                    }
            } else {
                Text(
                    text = "No active reminder",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }
    }
}