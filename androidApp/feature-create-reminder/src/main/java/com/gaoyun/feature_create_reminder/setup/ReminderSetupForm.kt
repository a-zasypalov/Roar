package com.gaoyun.feature_create_reminder.setup

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.gaoyun.common.dialog.DatePicker
import com.gaoyun.common.dialog.TimePicker
import com.gaoyun.common.ui.*
import com.gaoyun.roar.model.domain.interactions.*
import com.gaoyun.roar.util.toLocalDate
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.days

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ReminderSetupForm(
    template: InteractionTemplate?,
    repeatConfig: InteractionRepeatConfig,
    onConfigSave: (String) -> Unit,
    onSaveButtonClick: (String, InteractionType, InteractionGroup, Boolean, InteractionRepeatConfig, String, Long, Int, Int) -> Unit,
) {
    val activity = LocalContext.current as AppCompatActivity
    val ddMMMYYYYDateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    val interactionGroupState = rememberSaveable { mutableStateOf(template?.group?.toString() ?: InteractionGroup.ROUTINE_STRING) }
    val interactionRepeatConfigState = remember { mutableStateOf(repeatConfig) }
    val interactionRepeatConfigTextState = remember { mutableStateOf(TextFieldValue(repeatConfig.toString())) }

    val reminderName = rememberSaveable { mutableStateOf(template?.name ?: "") }
    val notesState = remember { mutableStateOf("") }

    val repeatEnabledState = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }

    val startsOnDate = remember { mutableStateOf(Clock.System.now().plus(1.days).toEpochMilliseconds()) }
    val startsOnDateString = remember {
        mutableStateOf(
            TextFieldValue(
                Instant.fromEpochMilliseconds(startsOnDate.value).toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
                    .format(ddMMMYYYYDateFormatter)
            )
        )
    }

    val startsOnTime = remember { mutableStateOf(LocalTime.parse("09:00")) }
    val startsOnTimeString = remember {
        mutableStateOf(
            TextFieldValue(
                StringBuilder()
                    .append(if (startsOnTime.value.hour < 10) "0${startsOnTime.value.hour}" else "${startsOnTime.value.hour}")
                    .append(":")
                    .append(if (startsOnTime.value.minute < 10) "0${startsOnTime.value.minute}" else "${startsOnTime.value.minute}")
                    .toString()
            )
        )
    }

    if (showDialog.value) {
        RepeatConfigDialog(
            if (interactionRepeatConfigState.value.active) interactionRepeatConfigState.value else null,
            setShowDialog = { showDialog.value = it },
            onConfigSave = {
                onConfigSave(it)
                interactionRepeatConfigTextState.value = TextFieldValue(it)

            })
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        TextFormField(
            text = reminderName.value,
            leadingIcon = {
                Icon(
                    Icons.Filled.TaskAlt,
                    "Name",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            label = "Name",
            onChange = {
                reminderName.value = it
            },
            imeAction = ImeAction.Done,
            modifier = Modifier.padding(horizontal = 24.dp),
        )

        Spacer(size = 16.dp)

        if (template == null) {
            DropdownMenu(
                valueList = InteractionGroup.GROUP_LIST,
                listState = interactionGroupState,
                label = "Group",
                leadingIcon = Icons.Filled.FormatListBulleted,
                modifier = Modifier.padding(horizontal = 24.dp),
            )

            Spacer(size = 16.dp)
        }

        ReadonlyTextField(
            value = startsOnTimeString.value,
            onValueChange = { startsOnTimeString.value = it },
            label = { Text(text = "Time") },
            onClick = {
                TimePicker.pickTime(
                    title = "Remind at",
                    fragmentManager = activity.supportFragmentManager,
                    hourAndMinutes = listOf(
                        startsOnTimeString.value.text.split(":")[0].toInt(),
                        startsOnTimeString.value.text.split(":")[1].toInt()
                    ),
                    onTimePicked = { hours, minutes ->
                        val hoursFormatted = if (hours < 10) "0$hours" else "$hours"
                        val minutesFormatted = if (minutes < 10) "0$minutes" else "$minutes"
                        val newTime = "$hoursFormatted:$minutesFormatted"
                        startsOnTimeString.value = TextFieldValue(newTime)
                        startsOnTime.value = LocalTime.parse(newTime)
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        Spacer(size = 16.dp)

        ReadonlyTextField(
            value = startsOnDateString.value,
            onValueChange = { startsOnDateString.value = it },
            label = { Text(text = "Date") },
            onClick = {
                DatePicker.pickDate(
                    title = "Remind on/from",
                    fragmentManager = activity.supportFragmentManager,
                    selectedDateMillis = startsOnDate.value,
                    start = Clock.System.now().toEpochMilliseconds(),
                    onDatePicked = {
                        startsOnDate.value = it
                        startsOnDateString.value = TextFieldValue(
                            Instant.fromEpochMilliseconds(it)
                                .toLocalDate()
                                .toJavaLocalDate()
                                .format(ddMMMYYYYDateFormatter)
                        )
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        Spacer(size = 16.dp)

        LabelledCheckBox(
            checked = repeatEnabledState.value,
            onCheckedChange = { repeatEnabledState.value = it },
            label = "Enable Repeat",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
        )

        Spacer(size = 8.dp)

        if (repeatEnabledState.value) {
            ReadonlyTextField(
                value = interactionRepeatConfigTextState.value,
                onValueChange = { },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Repeat,
                        "Repeat",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                label = {
                    Text(text = "Repeat")
                },
                onClick = { showDialog.value = true },
                modifier = Modifier.padding(horizontal = 24.dp),
            )

            Spacer(size = 16.dp)
        }

        TextFormField(
            text = notesState.value,
            onChange = { notesState.value = it },
            label = "Notes",
            leadingIcon = {
                Icon(Icons.Filled.Notes, "Notes")
            },
            imeAction = ImeAction.Done,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .onFocusEvent {
                    if (it.isFocused) {
                        coroutineScope.launch {
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                }
        )

        Spacer(size = 48.dp)

        PrimaryElevatedButton(
            text = "Create",
            onClick = {
                onSaveButtonClick(
                    reminderName.value,
                    template?.type ?: InteractionType.CUSTOM,
                    interactionGroupState.value.toInteractionGroup(),
                    repeatEnabledState.value,
                    interactionRepeatConfigState.value,
                    notesState.value,
                    startsOnDate.value,
                    startsOnTime.value.hour,
                    startsOnTime.value.minute
                )
            },
            modifier = Modifier
                .padding(bottom = 32.dp)
        )
    }
}