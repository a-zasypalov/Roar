package com.gaoyun.roar.ui.features.interactions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.presentation.BackNavigationEffect
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.interactions.InteractionScreenContract
import com.gaoyun.roar.presentation.interactions.InteractionScreenViewModel
import com.gaoyun.roar.ui.Spacer
import com.gaoyun.roar.ui.SurfaceScaffold
import com.gaoyun.roar.ui.common.composables.BoxWithLoader
import com.gaoyun.roar.ui.common.composables.RoarExtendedFAB
import com.gaoyun.roar.ui.common.dialog.InteractionCompletionDialog
import com.gaoyun.roar.util.SharedDateUtils
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun InteractionScreenDestination(
    onNavigationCall: (NavigationSideEffect) -> Unit,
    interactionId: String
) {
    val viewModel = koinViewModel(vmClass = InteractionScreenViewModel::class)
    val state = viewModel.viewState.collectAsState().value
    val notesState = rememberSaveable { mutableStateOf(state.interaction?.notes) }

    if (notesState.value == null && !state.interaction?.notes.isNullOrEmpty()) {
        notesState.value = state.interaction?.notes.orEmpty()
    }

    LaunchedEffect(Unit) {
        //TODO: onCreate
        viewModel.buildScreenState(interactionId)
        //TODO: onPause
        //viewModel.setEvent(InteractionScreenContract.Event.OnSaveNotes(notesState.value ?: ""))
    }

    val showRemoveInteractionDialog = remember { mutableStateOf(false) }
    val showRemoveReminderFromHistoryDialog = remember { mutableStateOf(false) }
    val reminderToRemoveId = remember { mutableStateOf<String?>(null) }

    val showCompleteReminderDateDialog = remember { mutableStateOf(false) }
    val completeReminderDateDialogDate = remember { mutableStateOf(SharedDateUtils.currentDateTime()) }
    val reminderToCompleteId = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        viewModel.effect.onEach { effect ->
            when (effect) {
                is InteractionScreenContract.Effect.NavigateBack -> onNavigationCall(BackNavigationEffect)
                is InteractionScreenContract.Effect.Navigation -> onNavigationCall(effect)
                is InteractionScreenContract.Effect.ShowRemoveReminderFromHistoryDialog -> {
                    reminderToRemoveId.value = effect.reminderId
                    showRemoveReminderFromHistoryDialog.value = true
                }

                is InteractionScreenContract.Effect.ShowRemoveInteractionDialog -> {
                    showRemoveInteractionDialog.value = true
                }

                is InteractionScreenContract.Effect.ShowCompleteReminderDialog -> {
                    completeReminderDateDialogDate.value = effect.date
                    reminderToCompleteId.value = effect.reminderId
                    showCompleteReminderDateDialog.value = true
                }
            }
        }.collect()
    }

    SurfaceScaffold(
        backHandler = { onNavigationCall(BackNavigationEffect) },
        floatingActionButton = {
            state.interaction?.let { interaction ->
                if (interaction.isActive) {
                    RoarExtendedFAB(
                        icon = Icons.Filled.Edit,
                        contentDescription = "", //stringResource(id = R.string.edit),
                        text = "Edit", //stringResource(id = R.string.edit),
                        onClick = {
                            viewModel.setEvent(
                                InteractionScreenContract.Event.OnEditClick(
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
        when {
            showRemoveReminderFromHistoryDialog.value -> {
                AlertDialog(
                    onDismissRequest = {
                        showRemoveReminderFromHistoryDialog.value = false
                        reminderToRemoveId.value = null
                    },
                    title = {
                        Text(
                            "Are you sure?" //stringResource(id = R.string.are_you_sure)
                        )
                    },
                    text = {
                        Text(
                            "Delete reminder from history"
                            //stringResource(id = R.string.delete_reminder_from_history_confirmation_text)
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            showRemoveReminderFromHistoryDialog.value = false
                            reminderToRemoveId.value?.let {
                                viewModel.setEvent(InteractionScreenContract.Event.OnReminderRemoveFromHistoryClick(reminderId = it, confirmed = true))
                            }
                            reminderToRemoveId.value = null
                        }) {
                            Text(
                                "Yes"
//                                stringResource(id = R.string.yes)
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showRemoveReminderFromHistoryDialog.value = false
                            reminderToRemoveId.value = null
                        }) {
                            Text(
                                "Cancel"
//                                stringResource(id = R.string.cancel)
                            )
                        }
                    }
                )
            }

            showRemoveInteractionDialog.value -> {
                AlertDialog(
                    onDismissRequest = { showRemoveInteractionDialog.value = false },
                    title = {
                        Text(
                            "Are you sure?"
//                        stringResource(id = R.string.are_you_sure)
                        )
                    },
                    text = {
                        Text(
                            "Delete reminder completely?"
//                        stringResource(id = R.string.delete_reminder_completely_confirmation_text)
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            showRemoveInteractionDialog.value = false
                            state.interaction?.let { interaction ->
                                viewModel.setEvent(InteractionScreenContract.Event.OnDeleteButtonClick(interactionId = interaction.id, confirmed = true))
                            }
                        }) {
                            Text(
                                "Yes"
//                                stringResource(id = R.string.yes)
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showRemoveInteractionDialog.value = false
                        }) {
                            Text(
                                "Cancel"
//                                stringResource(id = R.string.cancel)
                            )
                        }
                    }
                )
            }

            showCompleteReminderDateDialog.value -> {
                InteractionCompletionDialog(
                    showCompleteReminderDateDialog = showCompleteReminderDateDialog,
                    dateTime = completeReminderDateDialogDate.value,
                    onConfirmButtonClick = {
                        showCompleteReminderDateDialog.value = false
                        viewModel.setEvent(
                            InteractionScreenContract.Event.OnReminderCompleteClick(
                                reminderId = reminderToCompleteId.value ?: "",
                                isComplete = true,
                                completionDateTime = SharedDateUtils.currentDateAt(
                                    hour = completeReminderDateDialogDate.value.hour,
                                    minute = completeReminderDateDialogDate.value.minute
                                )
                            )
                        )
                    },
                    onDismissButtonClick = {
                        showCompleteReminderDateDialog.value = false
                        viewModel.setEvent(
                            InteractionScreenContract.Event.OnReminderCompleteClick(
                                reminderId = reminderToCompleteId.value ?: "",
                                isComplete = true,
                                completionDateTime = completeReminderDateDialogDate.value
                            )
                        )
                    }
                )
            }
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
                                    text = "Next", //stringResource(id = R.string.next),
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
                                )
                            }

                            item {
                                ReminderItems(
                                    reminders = nextReminders,
                                    onReminderCompleteClick = viewModel::setEvent,
                                    onCompleteReminderNotTodayClick = viewModel::setEvent
                                )
                            }
                        }

                        if (completeReminders.isNotEmpty()) {
                            item {
                                Text(
                                    text = "History", //stringResource(id = R.string.history),
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
                                )
                            }

                            item {
                                CompleteReminderItems(
                                    reminders = completeReminders,
                                    onReminderCompleteClick = viewModel::setEvent,
                                    onReminderRemoveFromHistoryClick = viewModel::setEvent,
                                )
                            }
                        }

                        item { Spacer(size = 32.dp) }

                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                TextButton(onClick = {
                                    viewModel.setEvent(
                                        InteractionScreenContract.Event.OnActivateButtonClick(
                                            interactionId = interaction.id,
                                            activate = !interaction.isActive
                                        )
                                    )
                                }) {
                                    Text(
                                        text = "Activate/Deactivate", //if (interaction.isActive) stringResource(id = R.string.deactivate) else stringResource(id = R.string.reactivate),
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
                                    )
                                }

                                Spacer(8.dp)

                                TextButton(onClick = { viewModel.setEvent(InteractionScreenContract.Event.OnDeleteButtonClick(interactionId = interaction.id)) }) {
                                    Text(
                                        text = "Delete interaction", //stringResource(id = R.string.delete_interaction),
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