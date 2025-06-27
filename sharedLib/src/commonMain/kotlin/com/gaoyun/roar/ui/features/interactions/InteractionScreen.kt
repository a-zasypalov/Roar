package com.gaoyun.roar.ui.features.interactions

import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.presentation.interactions.InteractionScreenContract
import com.gaoyun.roar.presentation.interactions.InteractionScreenViewModel
import com.gaoyun.roar.ui.common.composables.BoxWithLoader
import com.gaoyun.roar.ui.common.composables.RoarExtendedFAB
import com.gaoyun.roar.ui.common.composables.Spacer
import com.gaoyun.roar.ui.common.composables.SurfaceScaffold
import com.gaoyun.roar.ui.common.dialog.InteractionCompletionDialog
import com.gaoyun.roar.ui.navigation.BackNavigationEffect
import com.gaoyun.roar.ui.navigation.NavigationSideEffect
import com.gaoyun.roar.util.SharedDateUtils
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.are_you_sure
import roar.sharedlib.generated.resources.cancel
import roar.sharedlib.generated.resources.deactivate
import roar.sharedlib.generated.resources.delete_interaction
import roar.sharedlib.generated.resources.delete_reminder_completely_confirmation_text
import roar.sharedlib.generated.resources.delete_reminder_from_history_confirmation_text
import roar.sharedlib.generated.resources.edit
import roar.sharedlib.generated.resources.history
import roar.sharedlib.generated.resources.next
import roar.sharedlib.generated.resources.reactivate
import roar.sharedlib.generated.resources.yes

@Composable
fun InteractionScreenDestination(
    onNavigationCall: (NavigationSideEffect) -> Unit,
    interactionId: String
) {
    val viewModel = koinViewModel<InteractionScreenViewModel>()
    val state by viewModel.viewState.collectAsState()

    val notesState = rememberSaveable { mutableStateOf(state.interaction?.notes) }
    val savedNote = remember { mutableStateOf(state.interaction?.notes) }

    if (notesState.value == null && !state.interaction?.notes.isNullOrEmpty()) {
        notesState.value = state.interaction?.notes.orEmpty()
        savedNote.value = state.interaction?.notes.orEmpty()
    }

    LaunchedEffect(Unit) {
        viewModel.buildScreenState(interactionId)
    }

//    BackHandler { onNavigationCall(BackNavigationEffect) }

    // Local UI states for dialogs
    val showRemoveReminderDialog = remember { mutableStateOf(false) }
    val reminderToRemoveId = remember { mutableStateOf<String?>(null) }
    val showCompleteReminderDialog = remember { mutableStateOf(false) }
    val completeReminderDate = remember { mutableStateOf(SharedDateUtils.currentDateTime()) }
    val reminderToCompleteId = remember { mutableStateOf<String?>(null) }
    val showDeleteInteractionDialog = remember { mutableStateOf(false) }

    SurfaceScaffold(
        backHandler = { onNavigationCall(BackNavigationEffect) },
        floatingActionButton = {
            state.interaction?.let { interaction ->
                if (interaction.isActive) {
                    RoarExtendedFAB(
                        icon = Icons.Filled.Edit,
                        contentDescription = stringResource(Res.string.edit),
                        text = stringResource(Res.string.edit),
                        onClick = {
                            state.pet?.let { pet ->
                                onNavigationCall(InteractionScreenContract.Effect.Navigation.ToEditInteraction(pet.id, interaction))
                            }
                        }
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {

        // Remove Reminder dialog
        if (showRemoveReminderDialog.value) {
            AlertDialog(
                onDismissRequest = { showRemoveReminderDialog.value = false },
                title = { Text(stringResource(Res.string.are_you_sure)) },
                text = { Text(stringResource(Res.string.delete_reminder_from_history_confirmation_text)) },
                confirmButton = {
                    TextButton(onClick = {
                        reminderToRemoveId.value?.let {
                            viewModel.removeReminderFromHistory(it)
                        }
                        showRemoveReminderDialog.value = false
                    }) { Text(stringResource(Res.string.yes)) }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showRemoveReminderDialog.value = false
                    }) { Text(stringResource(Res.string.cancel)) }
                }
            )
        }

        // Complete Reminder dialog
        if (showCompleteReminderDialog.value) {
            InteractionCompletionDialog(
                showCompleteReminderDateDialog = showCompleteReminderDialog,
                dateTime = completeReminderDate.value,
                onConfirmButtonClick = {
                    showCompleteReminderDialog.value = false
                    viewModel.onReminderComplete(
                        reminderToCompleteId.value ?: "",
                        isComplete = true,
                        completionDateTime = SharedDateUtils.currentDateAt(
                            hour = completeReminderDate.value.hour,
                            minute = completeReminderDate.value.minute
                        )
                    )
                },
                onDismissButtonClick = {
                    showCompleteReminderDialog.value = false
                    viewModel.onReminderComplete(
                        reminderToCompleteId.value ?: "",
                        isComplete = true,
                        completionDateTime = completeReminderDate.value
                    )
                }
            )
        }

        // Delete Interaction dialog
        if (showDeleteInteractionDialog.value) {
            AlertDialog(
                onDismissRequest = { showDeleteInteractionDialog.value = false },
                title = { Text(stringResource(Res.string.are_you_sure)) },
                text = { Text(stringResource(Res.string.delete_reminder_completely_confirmation_text)) },
                confirmButton = {
                    TextButton(onClick = {
                        state.interaction?.id?.let {
                            viewModel.onDeleteInteraction(it) {
                                onNavigationCall(BackNavigationEffect)
                            }
                        }
                        showDeleteInteractionDialog.value = false
                    }) { Text(stringResource(Res.string.yes)) }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDeleteInteractionDialog.value = false
                    }) { Text(stringResource(Res.string.cancel)) }
                }
            )
        }

        BoxWithLoader(isLoading = state.isLoading) {
            state.interaction?.let { interaction ->
                state.pet?.let { pet ->
                    val nextReminders = interaction.reminders.filter { !it.isCompleted }
                    val completedReminders = interaction.reminders.filter { it.isCompleted }
                        .sortedByDescending { it.dateTime }

                    LazyColumn(modifier = Modifier.padding(horizontal = 8.dp)) {
                        item {
                            InteractionHeader(
                                pet = pet,
                                interaction = interaction,
                                notesState = notesState,
                                savedNote = savedNote,
                                onSaveNoteClick = {
                                    viewModel.onSaveNotes(notesState.value ?: "")
                                },
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        if (nextReminders.isNotEmpty()) {
                            item {
                                Text(
                                    text = stringResource(resource = Res.string.next),
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
                                )
                            }
                            item {
                                ReminderItems(
                                    reminders = nextReminders,
                                    onReminderCompleteClick = { event ->
                                        completeReminderDate.value = event.completionDateTime
                                        reminderToCompleteId.value = event.reminderId
                                        showCompleteReminderDialog.value = true
                                    },
                                    onCompleteReminderNotTodayClick = { event ->
                                        completeReminderDate.value = event.date
                                        reminderToCompleteId.value = event.reminderId
                                        showCompleteReminderDialog.value = true
                                    }
                                )
                            }
                        }

                        if (completedReminders.isNotEmpty()) {
                            item {
                                Text(
                                    text = stringResource(resource = Res.string.history),
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
                                )
                            }
                            item {
                                CompleteReminderItems(
                                    reminders = completedReminders,
                                    onReminderCompleteClick = { event ->
                                        completeReminderDate.value = event.completionDateTime
                                        reminderToCompleteId.value = event.reminderId
                                        showCompleteReminderDialog.value = true
                                    },
                                    onReminderRemoveFromHistoryClick = { event ->
                                        reminderToRemoveId.value = event.reminderId
                                        showRemoveReminderDialog.value = event.confirmed
                                    }
                                )
                            }
                        }

                        item {
                            Spacer(32.dp)
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                TextButton(onClick = {
                                    viewModel.onActivateInteraction(
                                        interactionId = interaction.id,
                                        isActive = !interaction.isActive
                                    )
                                }) {
                                    Text(
                                        text = if (interaction.isActive)
                                            stringResource(Res.string.deactivate)
                                        else
                                            stringResource(Res.string.reactivate)
                                    )
                                }
                                Spacer(8.dp)
                                TextButton(onClick = {
                                    showDeleteInteractionDialog.value = true
                                }) {
                                    Text(
                                        text = stringResource(Res.string.delete_interaction),
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }

                        item { Spacer(120.dp) }
                    }
                }
            }
        }
    }
}