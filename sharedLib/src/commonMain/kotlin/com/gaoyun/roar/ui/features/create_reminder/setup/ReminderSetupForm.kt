package com.gaoyun.roar.ui.features.create_reminder.setup

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.model.domain.interactions.InteractionGroup
import com.gaoyun.roar.model.domain.interactions.InteractionRemindConfig
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfig
import com.gaoyun.roar.model.domain.interactions.InteractionTemplate
import com.gaoyun.roar.model.domain.interactions.InteractionType
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.model.domain.interactions.toInteractionGroup
import com.gaoyun.roar.ui.common.composables.LabelledCheckBox
import com.gaoyun.roar.ui.common.composables.PrimaryElevatedButton
import com.gaoyun.roar.ui.common.composables.ReadonlyTextField
import com.gaoyun.roar.ui.common.composables.Spacer
import com.gaoyun.roar.ui.common.composables.TextFormField
import com.gaoyun.roar.ui.common.ext.remindConfigTextFull
import com.gaoyun.roar.ui.common.ext.repeatConfigTextFull
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.create
import roar.sharedlib.generated.resources.date_time
import roar.sharedlib.generated.resources.enable_repeat
import roar.sharedlib.generated.resources.name
import roar.sharedlib.generated.resources.next_occurrence
import roar.sharedlib.generated.resources.notes
import roar.sharedlib.generated.resources.remind
import roar.sharedlib.generated.resources.repeat
import roar.sharedlib.generated.resources.save
import kotlin.time.Duration.Companion.days

@OptIn(ExperimentalFoundationApi::class, ExperimentalResourceApi::class)
@Composable
internal fun ReminderSetupForm(
    interactionToEdit: InteractionWithReminders?,
    template: InteractionTemplate?,
    repeatConfig: InteractionRepeatConfig,
    remindConfig: InteractionRemindConfig,
    snackbarHost: SnackbarHostState,
    onRepeatConfigSave: (String) -> Unit,
    onRemindConfigSave: (String) -> Unit,
    onSaveButtonClick: (String, InteractionType, InteractionGroup, Boolean, InteractionRepeatConfig, String, Long, Int, Int, InteractionRemindConfig) -> Unit,
) {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    val interactionGroupState = rememberSaveable {
        mutableStateOf(
            interactionToEdit?.group?.toString()
                ?: template?.group?.toString()
                ?: InteractionGroup.ROUTINE_STRING
        )
    }

    val reminderName = rememberSaveable {
        mutableStateOf(
            interactionToEdit?.name //?: template?.getName(activity) ?: ""
        )
    }
    val notesState = remember { mutableStateOf(interactionToEdit?.notes ?: "") }

    val repeatEnabledState = remember {
        mutableStateOf(interactionToEdit?.let { it.repeatConfig != null } ?: repeatConfig.active)
    }
    val showRepeatConfigDialog = remember { mutableStateOf(false) }
    val showRemindConfigDialog = remember { mutableStateOf(false) }

    val startsOnDate = remember {
        mutableStateOf(
            interactionToEdit?.reminders?.filter { !it.isCompleted }
                ?.maxByOrNull { it.dateTime }?.dateTime?.toInstant(TimeZone.currentSystemDefault())
                ?.toEpochMilliseconds()
                ?: Clock.System.now().plus(2.days).toEpochMilliseconds()
        )
    }
    val startsOnTime = remember {
        mutableStateOf(
            interactionToEdit?.reminders?.filter { !it.isCompleted }
                ?.maxByOrNull { it.dateTime }?.dateTime?.time
                ?: LocalTime.parse("09:00")
        )
    }
    val startsOnDateTimeString = remember {
        mutableStateOf(
            TextFieldValue(
                StringBuilder()
//                    .append(
//                        Instant.fromEpochMilliseconds(startsOnDate.value)
//                            .toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
//                            .format(ddMmmYyyyDateFormatter)
//                    )
                    .append(", ")
                    .append(if (startsOnTime.value.hour < 10) "0${startsOnTime.value.hour}" else "${startsOnTime.value.hour}")
                    .append(":")
                    .append(if (startsOnTime.value.minute < 10) "0${startsOnTime.value.minute}" else "${startsOnTime.value.minute}")
                    .toString()
            )
        )
    }

    if (showRepeatConfigDialog.value) {
        RepeatConfigDialog(
            if (repeatConfig.active) repeatConfig else null,
            setShowDialog = { showRepeatConfigDialog.value = it },
            onConfigSave = { onRepeatConfigSave(it) }
        )
    }

    if (showRemindConfigDialog.value) {
        RemindConfigDialog(
            remindConfig,
            setShowDialog = { showRemindConfigDialog.value = it },
            onConfigSave = { onRemindConfigSave(it) }
        )
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
    ) {
        Spacer(size = 12.dp)

        TextFormField(
            text = reminderName.value ?: "",
            leadingIcon = {
                Icon(
                    Icons.Filled.TaskAlt,
                    stringResource(resource = Res.string.name),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            label = stringResource(resource = Res.string.name),
            onChange = {
                reminderName.value = it
            },
            imeAction = ImeAction.Done,
            modifier = Modifier.padding(horizontal = 24.dp),
        )

        Spacer(size = 16.dp)

        if (template == null) {
//            DropdownMenu(
//                valueList = InteractionGroup.GROUP_LIST.map { it.toString() },
//                listState = interactionGroupState,
//                valueDisplayList = InteractionGroup.GROUP_LIST.map { it.toLocalizedStringId() },
//                listDisplayState = interactionGroupState.value.toInteractionGroup()
//                    .toLocalizedStringId(),
//                label = stringResource(id = R.string.group),
//                leadingIcon = Icons.AutoMirrored.Filled.FormatListBulleted,
//                modifier = Modifier.padding(horizontal = 24.dp),
//            )

            Spacer(size = 16.dp)
        }

        ReadonlyTextField(
            value = startsOnDateTimeString.value,
            onValueChange = { startsOnDateTimeString.value = it },
            label = {
                Text(
                    text = if (interactionToEdit != null) {
                        stringResource(resource = Res.string.next_occurrence)
                    } else {
                        stringResource(resource = Res.string.date_time)
                    }
                )
            },
            onClick = {
//                DatePicker.pickDate(
//                    title = activity.getString(R.string.remind_on_from),
//                    fragmentManager = activity.supportFragmentManager,
//                    selectedDateMillis = startsOnDate.value,
//                    start = Clock.System.now().toEpochMilliseconds(),
//                    onDatePicked = { newDate ->
//                        TimePicker
//                            .pickTime(
//                                title = activity.getString(R.string.remind_at),
//                                activity = activity,
//                                hourAndMinutes = listOf(
//                                    startsOnTime.value.hour,
//                                    startsOnTime.value.minute
//                                ),
//                                onTimePicked = { hours, minutes ->
//                                    val hoursFormatted = if (hours < 10) "0$hours" else "$hours"
//                                    val minutesFormatted =
//                                        if (minutes < 10) "0$minutes" else "$minutes"
//                                    val newTime = "$hoursFormatted:$minutesFormatted"
//
//                                    startsOnDate.value = newDate
//                                    startsOnTime.value = LocalTime.parse(newTime)
//
//                                    startsOnDateTimeString.value = TextFieldValue(
//                                        "${
//                                            Instant.fromEpochMilliseconds(newDate)
//                                                .toLocalDate()
//                                                .toJavaLocalDate()
//                                                .format(ddMmmYyyyDateFormatter)
//                                        }, $hoursFormatted:$minutesFormatted"
//                                    )
//                                }
//                            )
//                    }
//                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        Spacer(size = 16.dp)

        ReadonlyTextField(
            value = TextFieldValue(remindConfig.remindConfigTextFull()),
            onValueChange = { },
            leadingIcon = {
                Icon(
                    Icons.Outlined.Notifications,
                    stringResource(resource = Res.string.remind),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            label = {
                Text(text = stringResource(resource = Res.string.remind))
            },
            onClick = { showRemindConfigDialog.value = true },
            modifier = Modifier.padding(horizontal = 24.dp),
        )

        Spacer(size = 16.dp)

        LabelledCheckBox(
            checked = repeatEnabledState.value,
            onCheckedChange = { repeatEnabledState.value = it },
            label = stringResource(resource = Res.string.enable_repeat),
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
                        stringResource(resource = Res.string.repeat),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                label = {
                    Text(text = stringResource(resource = Res.string.repeat))
                },
                onClick = { showRepeatConfigDialog.value = true },
                modifier = Modifier.padding(horizontal = 24.dp),
            )

            Spacer(size = 16.dp)
        }

        TextFormField(
            text = notesState.value,
            onChange = { notesState.value = it },
            label = stringResource(resource = Res.string.notes),
            leadingIcon = {
                Icon(Icons.AutoMirrored.Filled.Notes, stringResource(resource = Res.string.notes))
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
            text = if (interactionToEdit != null) {
                stringResource(resource = Res.string.save)
            } else {
                stringResource(resource = Res.string.create)
            },
            onClick = {
                if ((reminderName.value ?: "").isBlank()) {
                    coroutineScope.launch { snackbarHost.showSnackbar("Name shouldn't be empty") } //TODO: Localize
                } else {
                    onSaveButtonClick(
                        reminderName.value ?: "",
                        template?.type ?: InteractionType.CUSTOM,
                        interactionGroupState.value.toInteractionGroup(),
                        repeatEnabledState.value,
                        repeatConfig,
                        notesState.value,
                        startsOnDate.value,
                        startsOnTime.value.hour,
                        startsOnTime.value.minute,
                        remindConfig
                    )
                }
            },
            modifier = Modifier
                .padding(bottom = 32.dp)
        )
    }
}