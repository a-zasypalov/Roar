package com.gaoyun.roar.ui.features.pet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.gaoyun.roar.model.domain.PetWithInteractions
import com.gaoyun.roar.model.domain.withInteractions
import com.gaoyun.roar.ui.common.composables.BoxWithLoader
import com.gaoyun.roar.ui.common.composables.RoarExtendedFAB
import com.gaoyun.roar.ui.common.composables.SurfaceScaffold
import com.gaoyun.roar.ui.common.dialog.InteractionCompletionDialog
import com.gaoyun.roar.ui.common.dialog.RemovePetConfirmationDialog
import com.gaoyun.roar.ui.features.pet.composables.PetContainer
import com.gaoyun.roar.ui.navigation.BackNavigationEffect
import com.gaoyun.roar.ui.navigation.NavigationSideEffect
import com.gaoyun.roar.ui.theme.RoarThemePreview
import com.gaoyun.roar.util.SharedDateUtils
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.add_reminder
import roar.sharedlib.generated.resources.reminder

@Composable
fun PetScreenDestination(
    navigate: (NavigationSideEffect) -> Unit,
    petId: String,
) {
    val viewModel = koinViewModel<PetScreenViewModel>()
    val state by viewModel.viewState.collectAsState()

    LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {
        viewModel.loadPet(petId)
    }

    val showCompleteReminderDateDialog = remember { mutableStateOf(false) }
    val completeReminderDateDialogDate = remember { mutableStateOf(SharedDateUtils.currentDateTime()) }
    val reminderToCompleteId = remember { mutableStateOf<String?>(null) }

    val verticalScroll = rememberScrollState()
    var fabExtended by remember { mutableStateOf(true) }
    LaunchedEffect(verticalScroll) {
        var prev = 0
        snapshotFlow { verticalScroll.value }
            .collect {
                fabExtended = it * 2 <= prev
                prev = it
            }
    }

    SurfaceScaffold(
        floatingActionButton = {
            RoarExtendedFAB(
                icon = Icons.Filled.Add,
                contentDescription = stringResource(resource = Res.string.add_reminder),
                text = stringResource(resource = Res.string.reminder),
                extended = fabExtended,
                onClick = {
                    // direct navigation to interaction templates
                    state.pet?.id?.let { id ->
                        navigate(
                            PetScreenContract.Effect.Navigation.ToInteractionTemplates(id)
                        )
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        backHandler = { navigate(BackNavigationEffect) }
    ) {
        if (state.deletePetDialogShow) {
            RemovePetConfirmationDialog(
                petName = state.pet?.name.orEmpty(),
                onDismiss = viewModel::hideDeleteConfirmDialog,
                onConfirm = {
                    viewModel.confirmDelete()
                    navigate(BackNavigationEffect)
                }
            )
        }

        if (showCompleteReminderDateDialog.value) {
            InteractionCompletionDialog(
                showCompleteReminderDateDialog = showCompleteReminderDateDialog,
                dateTime = completeReminderDateDialogDate.value,
                onConfirmButtonClick = {
                    showCompleteReminderDateDialog.value = false
                    viewModel.completeReminder(
                        reminderId = reminderToCompleteId.value ?: "",
                        isComplete = true,
                        dateTime = SharedDateUtils.currentDateAt(
                            hour = completeReminderDateDialogDate.value.hour,
                            minute = completeReminderDateDialogDate.value.minute,
                        )
                    )
                },
                onDismissButtonClick = {
                    showCompleteReminderDateDialog.value = false
                    viewModel.completeReminder(
                        reminderId = reminderToCompleteId.value ?: "",
                        isComplete = true,
                        dateTime = completeReminderDateDialogDate.value
                    )
                }
            )
        }

        BoxWithLoader(isLoading = state.isLoading) {
            state.pet?.let { pet ->
                PetContainer(
                    pet = pet.withInteractions(state.interactions),
                    inactiveInteractions = state.inactiveInteractions,
                    onInteractionClick = { interactionId ->
                        navigate(
                            PetScreenContract.Effect.Navigation.ToInteractionDetails(interactionId)
                        )
                    },
                    onDeletePetClick = { viewModel.showDeleteConfirmDialog() },
                    onEditPetClick = {
                        navigate(
                            PetScreenContract.Effect.Navigation.ToEditPet(pet)
                        )
                    },
                    onInteractionCheckClicked = { reminderId, completed, completionDateTime ->
                        if (completed) {
                            reminderToCompleteId.value = reminderId
                            completeReminderDateDialogDate.value = completionDateTime
                            showCompleteReminderDateDialog.value = true
                        } else {
                            viewModel.completeReminder(reminderId, false, completionDateTime)
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
@Preview
fun PetScreenPreview() {
    RoarThemePreview {
        PetContainer(PetWithInteractions.preview(), listOf(), {}, {}, {}, { _, _, _ -> })
    }
}