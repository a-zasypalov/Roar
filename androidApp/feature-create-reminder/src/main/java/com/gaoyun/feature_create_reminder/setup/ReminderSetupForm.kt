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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.gaoyun.common.DateUtils.ddMmmYyyyDateFormatter
import com.gaoyun.common.R
import com.gaoyun.common.dialog.DatePicker
import com.gaoyun.common.dialog.TimePicker
import com.gaoyun.common.ext.getName
import com.gaoyun.common.ext.repeatConfigTextFull
import com.gaoyun.common.ext.toLocalizedStringId
import com.gaoyun.common.ui.*
import com.gaoyun.roar.model.domain.interactions.*
import com.gaoyun.roar.util.toLocalDate
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlin.time.Duration.Companion.days

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ReminderSetupForm(
    interactionToEdit: InteractionWithReminders?,
    template: InteractionTemplate?,
    repeatConfig: InteractionRepeatConfig,
    onConfigSave: (String) -> Unit,
    onSaveButtonClick: (String, InteractionType, InteractionGroup, Boolean, InteractionRepeatConfig, String, Long, Int, Int) -> Unit,
) {
    val activity = LocalContext.current as AppCompatActivity
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    val interactionGroupState = rememberSaveable {
        mutableStateOf(
            interactionToEdit?.group?.toString()
                ?: template?.group?.toString()
                ?: InteractionGroup.ROUTINE_STRING
        )
    }

    val reminderName = rememberSaveable { mutableStateOf(interactionToEdit?.name ?: template?.getName(activity) ?: "") }
    val notesState = remember { mutableStateOf(interactionToEdit?.notes ?: "") }

    val repeatEnabledState = remember { mutableStateOf(interactionToEdit?.let { it.repeatConfig != null } ?: repeatConfig.active) }
    val showDialog = remember { mutableStateOf(false) }

    val startsOnDate = remember {
        mutableStateOf(
            interactionToEdit?.reminders?.filter { !it.isCompleted }?.maxByOrNull { it.dateTime }?.dateTime?.toInstant(TimeZone.currentSystemDefault())?.toEpochMilliseconds()
                ?: Clock.System.now().plus(1.days).toEpochMilliseconds()
        )
    }
    val startsOnDateString = remember {
        mutableStateOf(
            TextFieldValue(
                Instant.fromEpochMilliseconds(startsOnDate.value).toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
                    .format(ddMmmYyyyDateFormatter)
            )
        )
    }

    val startsOnTime = remember {
        mutableStateOf(
            interactionToEdit?.reminders?.filter { !it.isCompleted }?.maxByOrNull { it.dateTime }?.dateTime?.time
                ?: LocalTime.parse("09:00")
        )
    }
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
            if (repeatConfig.active) repeatConfig else null,
            setShowDialog = { showDialog.value = it },
            onConfigSave = {
                onConfigSave(it)
            })
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(size = 8.dp)

        TextFormField(
            text = reminderName.value,
            leadingIcon = {
                Icon(
                    Icons.Filled.TaskAlt,
                    stringResource(id = R.string.name),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            label = stringResource(id = R.string.name),
            onChange = {
                reminderName.value = it
            },
            imeAction = ImeAction.Done,
            modifier = Modifier.padding(horizontal = 24.dp),
        )

        Spacer(size = 16.dp)

        if (template == null) {
            DropdownMenu(
                valueList = InteractionGroup.GROUP_LIST.map { it.toString() },
                listState = interactionGroupState,
                valueDisplayList = InteractionGroup.GROUP_LIST.map { it.toLocalizedStringId() },
                listDisplayState = interactionGroupState.value.toInteractionGroup().toLocalizedStringId(),
                label = stringResource(id = R.string.group),
                leadingIcon = Icons.Filled.FormatListBulleted,
                modifier = Modifier.padding(horizontal = 24.dp),
            )

            Spacer(size = 16.dp)
        }

        ReadonlyTextField(
            value = startsOnTimeString.value,
            onValueChange = { startsOnTimeString.value = it },
            label = { Text(text = stringResource(id = R.string.time)) },
            onClick = {
                TimePicker.pickTime(
                    title = activity.getString(R.string.remind_at),
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
            label = { Text(text = if (interactionToEdit != null) stringResource(id = R.string.next_reminder) else stringResource(id = R.string.date)) },
            onClick = {
                DatePicker.pickDate(
                    title = activity.getString(R.string.remind_on_from),
                    fragmentManager = activity.supportFragmentManager,
                    selectedDateMillis = startsOnDate.value,
                    start = Clock.System.now().toEpochMilliseconds(),
                    onDatePicked = {
                        startsOnDate.value = it
                        startsOnDateString.value = TextFieldValue(
                            Instant.fromEpochMilliseconds(it)
                                .toLocalDate()
                                .toJavaLocalDate()
                                .format(ddMmmYyyyDateFormatter)
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
            label = stringResource(id = R.string.enable_repeat),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
        )

        Spacer(size = 8.dp)

        if (repeatEnabledState.value) {
            ReadonlyTextField(
                value = TextFieldValue(repeatConfig.repeatConfigTextFull()),
                onValueChange = { },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Repeat,
                        stringResource(id = R.string.repeat),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                label = {
                    Text(text = stringResource(id = R.string.repeat))
                },
                onClick = { showDialog.value = true },
                modifier = Modifier.padding(horizontal = 24.dp),
            )

            Spacer(size = 16.dp)
        }

        TextFormField(
            text = notesState.value,
            onChange = { notesState.value = it },
            label = stringResource(id = R.string.notes),
            leadingIcon = {
                Icon(Icons.Filled.Notes, stringResource(id = R.string.notes))
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
            text = if (interactionToEdit != null) stringResource(id = R.string.save) else stringResource(id = R.string.create),
            onClick = {
                onSaveButtonClick(
                    reminderName.value,
                    template?.type ?: InteractionType.CUSTOM,
                    interactionGroupState.value.toInteractionGroup(),
                    repeatEnabledState.value,
                    repeatConfig,
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