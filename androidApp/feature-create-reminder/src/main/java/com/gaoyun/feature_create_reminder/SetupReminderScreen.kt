package com.gaoyun.feature_create_reminder

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
                                onSaveButtonClick = {
                                    onEventSent(SetupReminderScreenContract.Event.OnSaveButtonClick(""))
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
    onSaveButtonClick: () -> Unit,
) {
    val interactionTypeState = rememberSaveable { mutableStateOf(template?.type?.toString() ?: "") }
    val interactionRepeatConfigState = remember { mutableStateOf(template?.repeatConfig ?: InteractionRepeatConfig()) }
    val interactionRepeatConfigTextState = remember { mutableStateOf(TextFieldValue()) }

    val reminderName = rememberSaveable { mutableStateOf(template?.name ?: "") }
    val notesState = remember { mutableStateOf("") }

    val repeatEnabledState = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        RepeatConfigDialog(
            if (repeatConfig.active) repeatConfig else null,
            setShowDialog = { showDialog.value = it },
            onConfigSave = onConfigSave
        )
    }

    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

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
                onClick = {},
                modifier = Modifier.padding(horizontal = 24.dp),
            )
        } ?: DropdownMenu(
            valueList = InteractionType.TYPES_LIST,
            listState = interactionTypeState,
            label = "Type",
            leadingIcon = Icons.Filled.List,
            modifier = Modifier.padding(horizontal = 24.dp),
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

        Spacer(size = 16.dp)

        if (repeatEnabledState.value) {
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
                    Text(text = if (repeatConfig.active) "Repeat: $repeatConfig" else "Repeat")
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
            onClick = { onSaveButtonClick() },
            modifier = Modifier
                .padding(bottom = 32.dp)
        )
    }
}