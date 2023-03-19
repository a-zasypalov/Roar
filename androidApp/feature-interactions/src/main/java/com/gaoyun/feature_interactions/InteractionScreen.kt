package com.gaoyun.feature_interactions

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.gaoyun.common.DateUtils
import com.gaoyun.common.NavigationKeys
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.R
import com.gaoyun.common.dialog.InteractionCompletionDialog
import com.gaoyun.common.ui.*
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.interactions.InteractionScreenContract
import com.gaoyun.roar.presentation.interactions.InteractionScreenViewModel
import com.gaoyun.roar.util.toLocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import org.koin.androidx.compose.getViewModel
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun InteractionScreenDestination(
    navHostController: NavHostController,
    interactionId: String
) {
    val viewModel: InteractionScreenViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value
    val notesState = rememberSaveable { mutableStateOf(state.interaction?.notes) }

    if (notesState.value == null && !state.interaction?.notes.isNullOrEmpty()) {
        notesState.value = state.interaction?.notes.orEmpty()
    }

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_CREATE) {
            viewModel.buildScreenState(interactionId)
        } else if (event == Lifecycle.Event.ON_PAUSE) {
            viewModel.setEvent(InteractionScreenContract.Event.OnSaveNotes(notesState.value ?: ""))
        }
    }

    InteractionScreen(
        state = state,
        effectFlow = viewModel.effect,
        notesState = notesState,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is InteractionScreenContract.Effect.Navigation.NavigateBack -> navHostController.navigateUp()
                is InteractionScreenContract.Effect.Navigation.ToEditInteraction -> navHostController.navigate("${NavigationKeys.Route.EDIT}/${NavigationKeys.Route.ADD_REMINDER}/${navigationEffect.petId}/${navigationEffect.interaction.templateId}/${navigationEffect.interaction.id}")
            }
        },
    )

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun InteractionScreen(
    state: InteractionScreenContract.State,
    effectFlow: Flow<InteractionScreenContract.Effect>,
    onEventSent: (event: InteractionScreenContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: InteractionScreenContract.Effect.Navigation) -> Unit,
    notesState: MutableState<String?>,
) {
    val showRemoveInteractionDialog = remember { mutableStateOf(false) }
    val showRemoveReminderFromHistoryDialog = remember { mutableStateOf(false) }
    val reminderToRemoveId = remember { mutableStateOf<String?>(null) }

    val showCompleteReminderDateDialog = remember { mutableStateOf(false) }
    val completeReminderDateDialogDate = remember { mutableStateOf<LocalDateTime>(LocalDateTime.now()) }
    val reminderToCompleteId = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow.onEach { effect ->
            when (effect) {
                is InteractionScreenContract.Effect.Navigation -> onNavigationRequested(effect)
                is InteractionScreenContract.Effect.ShowRemoveReminderFromHistoryDialog -> {
                    reminderToRemoveId.value = effect.reminderId
                    showRemoveReminderFromHistoryDialog.value = true
                }
                is InteractionScreenContract.Effect.ShowRemoveInteractionDialog -> {
                    showRemoveInteractionDialog.value = true
                }
                is InteractionScreenContract.Effect.ShowCompleteReminderDialog -> {
                    completeReminderDateDialogDate.value = effect.date.toJavaLocalDateTime()
                    reminderToCompleteId.value = effect.reminderId
                    showCompleteReminderDateDialog.value = true
                }
                else -> {}
            }
        }.collect()
    }

    SurfaceScaffold(
        backHandler = { onNavigationRequested(InteractionScreenContract.Effect.Navigation.NavigateBack) },
        floatingActionButton = {
            state.interaction?.let { interaction ->
                if (interaction.isActive) {
                    RoarExtendedFloatingActionButton(
                        icon = Icons.Filled.Edit,
                        contentDescription = stringResource(id = R.string.edit),
                        text = stringResource(id = R.string.edit),
                        onClick = {
                            onNavigationRequested(
                                InteractionScreenContract.Effect.Navigation.ToEditInteraction(
                                    petId = state.pet?.id ?: "",
                                    interaction = interaction
                                )
                            )
                        })
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        if (showRemoveReminderFromHistoryDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    showRemoveReminderFromHistoryDialog.value = false
                    reminderToRemoveId.value = null
                },
                title = { Text(stringResource(id = R.string.are_you_sure)) },
                text = { Text(stringResource(id = R.string.delete_reminder_from_history_confirmation_text)) },
                confirmButton = {
                    TextButton(onClick = {
                        showRemoveReminderFromHistoryDialog.value = false
                        reminderToRemoveId.value?.let {
                            onEventSent(InteractionScreenContract.Event.OnReminderRemoveFromHistoryClick(reminderId = it, confirmed = true))
                        }
                        reminderToRemoveId.value = null
                    }) {
                        Text(stringResource(id = R.string.yes))
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showRemoveReminderFromHistoryDialog.value = false
                        reminderToRemoveId.value = null
                    }) {
                        Text(stringResource(id = R.string.cancel))
                    }
                }
            )
        }

        if (showRemoveInteractionDialog.value) {
            AlertDialog(
                onDismissRequest = { showRemoveInteractionDialog.value = false },
                title = { Text(stringResource(id = R.string.are_you_sure)) },
                text = { Text(stringResource(id = R.string.delete_reminder_completely_confirmation_text)) },
                confirmButton = {
                    TextButton(onClick = {
                        showRemoveInteractionDialog.value = false
                        state.interaction?.let { interaction ->
                            onEventSent(InteractionScreenContract.Event.OnDeleteButtonClick(interactionId = interaction.id, confirmed = true))
                        }
                    }) {
                        Text(stringResource(id = R.string.yes))
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showRemoveInteractionDialog.value = false
                    }) {
                        Text(stringResource(id = R.string.cancel))
                    }
                }
            )
        }

        if (showCompleteReminderDateDialog.value) {
            var currentDateTime = LocalDateTime.now().withHour(completeReminderDateDialogDate.value.hour)
            currentDateTime = currentDateTime.withMinute(completeReminderDateDialogDate.value.minute)

            InteractionCompletionDialog(
                showCompleteReminderDateDialog = showCompleteReminderDateDialog,
                dateTime = completeReminderDateDialogDate.value,
                onConfirmButtonClick = {
                    showCompleteReminderDateDialog.value = false
                    onEventSent(
                        InteractionScreenContract.Event.OnReminderCompleteClick(
                            reminderId = reminderToCompleteId.value ?: "",
                            isComplete = true,
                            completionDateTime = currentDateTime.toKotlinLocalDateTime()
                        )
                    )
                },
                onDismissButtonClick = {
                    showCompleteReminderDateDialog.value = false
                    onEventSent(
                        InteractionScreenContract.Event.OnReminderCompleteClick(
                            reminderId = reminderToCompleteId.value ?: "",
                            isComplete = true,
                            completionDateTime = completeReminderDateDialogDate.value.toKotlinLocalDateTime()
                        )
                    )
                }
            )
        }

        BoxWithLoader(isLoading = state.isLoading) {
            state.interaction?.let { interaction ->
                state.pet?.let { pet ->
                    val nextReminders = interaction.reminders.filter { !it.isCompleted }
                    val completeReminders = interaction.reminders.filter { it.isCompleted }.sortedBy { it.dateTime }.reversed()

                    LazyColumn(modifier = Modifier.padding(horizontal = 8.dp)) {
                        item {
                            InteractionHeader(
                                pet = pet,
                                interaction = interaction,
                                notesState = notesState,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        if (nextReminders.isNotEmpty()) {
                            item {
                                Text(
                                    text = stringResource(id = R.string.next),
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
                                )
                            }

                            item {
                                Surface(
                                    tonalElevation = 4.dp,
                                    shape = MaterialTheme.shapes.large,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp, vertical = 8.dp)
                                        .fillMaxWidth(),
                                ) {
                                    Column {
                                        nextReminders.map { reminder ->
                                            LabelledCheckBox(
                                                checked = reminder.isCompleted,
                                                label = "${
                                                    if (reminder.dateTime.date.year != Clock.System.now().toLocalDate().year) {
                                                        reminder.dateTime.date.toJavaLocalDate().format(DateUtils.ddMmmmYyyyDateFormatter)
                                                    } else {
                                                        reminder.dateTime.date.toJavaLocalDate().format(DateUtils.ddMmmmDateFormatter)
                                                    }
                                                } ${stringResource(id = R.string.at)} ${
                                                    reminder.dateTime.toJavaLocalDateTime().format(DateUtils.hhMmTimeFormatter)
                                                }",
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalPadding = 16.dp,
                                                horizontalPadding = 20.dp,
                                                spacerSize = 14.dp,
                                                onCheckedChange = { isComplete ->
                                                    if (reminder.dateTime.date.toJavaLocalDate() == LocalDate.now()) {
                                                        onEventSent(
                                                            InteractionScreenContract.Event.OnReminderCompleteClick(
                                                                reminderId = reminder.id,
                                                                isComplete = isComplete,
                                                                completionDateTime = reminder.dateTime
                                                            )
                                                        )
                                                    } else {
                                                        onEventSent(
                                                            InteractionScreenContract.Event.OnCompleteReminderNotTodayClick(reminder.id, reminder.dateTime)
                                                        )
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        if (completeReminders.isNotEmpty()) {
                            item {
                                Text(
                                    text = stringResource(id = R.string.history),
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
                                )
                            }

                            item {
                                Surface(
                                    tonalElevation = 4.dp,
                                    shape = MaterialTheme.shapes.large,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp, vertical = 8.dp)
                                        .fillMaxWidth(),
                                ) {
                                    Column(Modifier.animateContentSize()) {
                                        completeReminders.mapIndexed { index, reminder ->
                                            LabelledCheckBox(
                                                checked = reminder.isCompleted,
                                                label = "${
                                                    if (reminder.dateTime.date.year != Clock.System.now().toLocalDate().year) {
                                                        reminder.dateTime.date.toJavaLocalDate().format(DateUtils.ddMmmmYyyyDateFormatter)
                                                    } else {
                                                        reminder.dateTime.date.toJavaLocalDate().format(DateUtils.ddMmmmDateFormatter)
                                                    }
                                                } ${stringResource(id = R.string.at)} ${
                                                    reminder.dateTime.toJavaLocalDateTime().format(DateUtils.hhMmTimeFormatter)
                                                }",
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalPadding = 16.dp,
                                                horizontalPadding = 20.dp,
                                                spacerSize = 14.dp,
                                                onCheckedChange = { isComplete ->
                                                    if (index == 0) {
                                                        onEventSent(
                                                            InteractionScreenContract.Event.OnReminderCompleteClick(
                                                                reminderId = reminder.id,
                                                                isComplete = isComplete,
                                                                completionDateTime = reminder.dateTime
                                                            )
                                                        )
                                                    } else {
                                                        onEventSent(
                                                            InteractionScreenContract.Event.OnReminderRemoveFromHistoryClick(reminder.id)
                                                        )
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        item { Spacer(size = 32.dp) }

                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                TextButton(onClick = {
                                    onEventSent(
                                        InteractionScreenContract.Event.OnActivateButtonClick(
                                            interactionId = interaction.id,
                                            activate = !interaction.isActive
                                        )
                                    )
                                }) {
                                    Text(
                                        text = if (interaction.isActive) stringResource(id = R.string.deactivate) else stringResource(id = R.string.reactivate),
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
                                    )
                                }

                                Spacer(8.dp)

                                TextButton(onClick = { onEventSent(InteractionScreenContract.Event.OnDeleteButtonClick(interactionId = interaction.id)) }) {
                                    Text(
                                        text = stringResource(id = R.string.delete_interaction),
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
                                    )
                                }
                            }
                        }

                        item { Spacer(size = 120.dp) }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun InteractionScreenPreview() {

}