package com.gaoyun.feature_create_reminder

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.ui.*
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfig
import com.gaoyun.roar.model.domain.interactions.InteractionTemplate
import com.gaoyun.roar.model.domain.interactions.InteractionType
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.add_reminder.setup_reminder.SetupReminderScreenContract
import com.gaoyun.roar.presentation.add_reminder.setup_reminder.SetupReminderScreenViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

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
    )

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SetupReminderScreen(
    state: SetupReminderScreenContract.State,
    effectFlow: Flow<SetupReminderScreenContract.Effect>,
    onEventSent: (event: SetupReminderScreenContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: SetupReminderScreenContract.Effect.Navigation) -> Unit,
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
        Box {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                state.pet?.let { pet ->
                    ReminderSetupHeader(
                        petAvatar = pet.avatar,
                        petName = pet.name
                    )
                    
                    Spacer(size = 32.dp)

                    ReminderSetupForm(state.template)
                }
            }
            Loader(isLoading = state.isLoading)
        }
    }
}

@Composable
private fun ReminderSetupHeader(petAvatar: String, petName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
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

@Composable
private fun ReminderSetupForm(template: InteractionTemplate?) {
    val reminderName = rememberSaveable { mutableStateOf(template?.name ?: "") }
    val interactionTypeState = rememberSaveable { mutableStateOf(template?.type?.toString() ?: "") }
    val interactionRepeatConfigState = remember { mutableStateOf(template?.repeatConfig ?: InteractionRepeatConfig()) }
    val interactionRepeatConfigTextState = remember { mutableStateOf(TextFieldValue()) }

    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        RepeatConfigDialog(setShowDialog = { showDialog.value = it })
    }

    Column {
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
            imeAction = ImeAction.Next,
        )

        Spacer(size = 16.dp)

        template?.let {
            ReadonlyTextField(
                value = TextFieldValue(it.type.toString()),
                onValueChange = { },
                leadingIcon = {
                    Icon(
                        Icons.Filled.FormatListBulleted,
                        "Type",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                label = {
                    Text(text = "Type")
                },
                onClick = {})
        } ?: DropdownMenu(
            valueList = InteractionType.TYPES_LIST,
            listState = interactionTypeState,
            label = "Type",
            leadingIcon = Icons.Filled.List
        )

        ReadonlyTextField(
            value = interactionRepeatConfigTextState.value,
            onValueChange = { interactionRepeatConfigTextState.value = it },
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
            onClick = { showDialog.value = true }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RepeatConfigDialog(
    setShowDialog: (Boolean) -> Unit,
) {
    val durationState = rememberSaveable { mutableStateOf("month") }
    val daysState = rememberSaveable { mutableStateOf("Day 1") }
    val endConditionState = rememberSaveable { mutableStateOf("After...") }
    val timeState = remember { mutableStateOf(TextFieldValue("19:00")) }
    val dateStartState = remember { mutableStateOf(TextFieldValue("15 November")) }
    val endsOnDateState = remember { mutableStateOf(TextFieldValue("15 November")) }
    var weekdaysSelectState = arrayListOf(false, false, false, false, false, false, false)

    val defaultHorizontalPadding = 24.dp

    Dialog(
        onDismissRequest = { setShowDialog(false) },
        properties = DialogProperties(
            dismissOnClickOutside = false
        )
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxSize()
        ) {
            SurfaceCard(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 36.dp)
            ) {
                Column(modifier = Modifier.padding(vertical = 16.dp)) {
                    Text(
                        text = "Repeats every",
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.padding(horizontal = defaultHorizontalPadding)
                    )

                    Spacer(size = 24.dp)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = defaultHorizontalPadding)
                    ) {
                        TextFormField(
                            text = "2",
                            keyboardType = KeyboardType.Decimal,
                            onChange = {},
                            modifier = Modifier.fillMaxWidth(0.25f)
                        )
                        Spacer(size = 12.dp)
                        DropdownMenu(
                            valueList = listOf("day", "week", "month", "year"),
                            listState = durationState,
                            modifier = Modifier.fillMaxWidth(1f)
                        )
                    }

                    Spacer(size = 8.dp)

                    when (durationState.value) {
                        "week" -> {
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                item {
                                    Spacer(size = 20.dp)
                                }
                                for (i in 0..6) {
                                    item {
                                        FilterChip(
                                            selected = weekdaysSelectState[i],
                                            onClick = {
                                                weekdaysSelectState = weekdaysSelectState.apply {
                                                    this[i] = !get(i)
                                                }
                                            },
                                            label = {
                                                Text(
                                                    "Mon",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                                )
                                            },
                                            modifier = Modifier.padding(horizontal = 4.dp)
                                        )
                                    }
                                }
                                item {
                                    Spacer(size = 20.dp)
                                }
                            }
                        }
                        "month" -> {
                            val days = arrayListOf<String>().apply {
                                for (i in 1..31) {
                                    add("Day $i")
                                }
                                add("Last Day")
                            }
                            DropdownMenu(
                                valueList = days,
                                listState = daysState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = defaultHorizontalPadding)
                            )
                            Spacer(size = 8.dp)
                        }
                    }

                    Spacer(size = 8.dp)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .padding(horizontal = defaultHorizontalPadding)
                            .background(MaterialTheme.colorScheme.outline)
                    )

                    Spacer(size = 16.dp)

                    ReadonlyTextField(
                        value = timeState.value,
                        onValueChange = { timeState.value = it },
                        label = { Text(text = "Time") },
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = defaultHorizontalPadding)
                    )

                    Spacer(size = 12.dp)

                    ReadonlyTextField(
                        value = dateStartState.value,
                        onValueChange = { dateStartState.value = it },
                        label = { Text(text = "Starts") },
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = defaultHorizontalPadding)
                    )

                    Spacer(size = 12.dp)

                    DropdownMenu(
                        valueList = listOf("Never", "On date", "After..."),
                        listState = endConditionState,
                        label = "Ends",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = defaultHorizontalPadding)
                    )

                    Spacer(size = 12.dp)

                    when (endConditionState.value) {
                        "On date" -> {
                            ReadonlyTextField(
                                value = endsOnDateState.value,
                                onValueChange = { endsOnDateState.value = it },
                                label = { Text(text = "End on date") },
                                onClick = { },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = defaultHorizontalPadding)
                            )
                        }
                        "After..." -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = defaultHorizontalPadding)
                            ) {
                                TextFormField(
                                    text = "2",
                                    keyboardType = KeyboardType.Decimal,
                                    onChange = {},
                                    modifier = Modifier.fillMaxWidth(0.25f)
                                )
                                Spacer(size = 12.dp)
                                Text(
                                    text = "occurrences",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.fillMaxWidth(1f)
                                )
                            }
                        }
                    }

                    Spacer(size = 12.dp)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = defaultHorizontalPadding),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { }) {
                            Text(text = "Cancel")
                        }

                        Spacer(size = 12.dp)

                        TextButton(onClick = { }) {
                            Text(text = "Done")
                        }
                    }
                }
            }
        }
    }
}