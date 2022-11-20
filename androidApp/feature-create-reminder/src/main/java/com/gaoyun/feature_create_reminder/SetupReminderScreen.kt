package com.gaoyun.feature_create_reminder

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.dialog.DatePicker
import com.gaoyun.common.dialog.TimePicker
import com.gaoyun.common.ui.*
import com.gaoyun.roar.model.domain.interactions.*
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.add_reminder.setup_reminder.SetupReminderScreenContract
import com.gaoyun.roar.presentation.add_reminder.setup_reminder.SetupReminderScreenViewModel
import com.gaoyun.roar.util.toLocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import org.koin.androidx.compose.getViewModel
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.days

@Composable
fun SetupReminderDestination(
    navHostController: NavHostController,
    petId: String,
    templateId: String
) {
    val viewModel: SetupReminderScreenViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_CREATE) {
            viewModel.buildScreenState(petId = petId, templateId = templateId)
        }
    }

    SetupReminderScreen(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is SetupReminderScreenContract.Effect.Navigation.NavigateBack -> navHostController.navigateUp()
            }
        },
        viewModel = viewModel
    )

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SetupReminderScreen(
    state: SetupReminderScreenContract.State,
    effectFlow: Flow<SetupReminderScreenContract.Effect>,
    onEventSent: (event: SetupReminderScreenContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: SetupReminderScreenContract.Effect.Navigation) -> Unit,
    viewModel: SetupReminderScreenViewModel
) {

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow.onEach { effect ->
            when (effect) {
//                is SetupReminderScreenContract.Effect.TemplateChosen -> {
//                    onNavigationRequested(AddReminderScreenContract.Effect.Navigation.ToReminderSetup(effect.templateId))
//                }
                else -> {}
            }
        }.collect()
    }

    SurfaceScaffold {
        Box(contentAlignment = Alignment.BottomCenter) {
            state.pet?.let { pet ->
                Box(modifier = Modifier.fillMaxSize()) {
                    ReminderSetupHeader(
                        petAvatar = pet.avatar,
                        petName = pet.name,
                    )
                }

                Column {
                    Spacer(size = 110.dp)
                    SurfaceCard(
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        modifier = Modifier.padding(horizontal = 12.dp)
                    ) {
                        Box(modifier = Modifier.padding(top = 32.dp)) {
                            ReminderSetupForm(
                                template = state.template,
                                repeatConfig = state.repeatConfig,
                                onConfigSave = { config ->
                                    onEventSent(SetupReminderScreenContract.Event.RepeatConfigChanged(config))
                                },
                                onSaveButtonClick = { name, type, group, repeatIsEnabled, repeatConfig, notes, date, timeHours, timeMinutes ->
                                    onEventSent(
                                        SetupReminderScreenContract.Event.OnSaveButtonClick(
                                            name = name,
                                            type = type,
                                            group = group,
                                            repeatIsEnabled = repeatIsEnabled,
                                            repeatConfig = repeatConfig,
                                            notes = notes,
                                            petId = pet.id,
                                            templateId = state.template?.id,
                                            date = date,
                                            timeHours = timeHours,
                                            timeMinutes = timeMinutes
                                        )
                                    )
                                },
                            )
                        }
                    }
                }
            }

            Loader(isLoading = state.isLoading)
        }
    }
}

@Composable
private fun ReminderSetupHeader(
    petAvatar: String,
    petName: String,
) {
    Row(
        modifier = Modifier
            .padding(top = 32.dp, start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = LocalContext.current.getDrawableByName(petAvatar)),
            contentDescription = petName,
            modifier = Modifier.size(48.dp)
        )

        Spacer(size = 10.dp)

        Text(
            text = "Reminder",
            style = MaterialTheme.typography.displayMedium,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ReminderSetupForm(
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
    val interactionRepeatConfigState = remember { mutableStateOf(template?.repeatConfig ?: InteractionRepeatConfig()) }
    val interactionRepeatConfigTextState = remember { mutableStateOf(TextFieldValue()) }

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
            if (repeatConfig.active) repeatConfig else null,
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