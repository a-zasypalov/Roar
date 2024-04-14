package com.gaoyun.feature_pet_screen

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.R
import com.gaoyun.common.composables.BoxWithLoader
import com.gaoyun.common.composables.RoarExtendedFAB
import com.gaoyun.common.composables.SurfaceScaffold
import com.gaoyun.common.dialog.InteractionCompletionDialog
import com.gaoyun.feature_pet_screen.view.PetContainer
import com.gaoyun.roar.model.domain.PetWithInteractions
import com.gaoyun.roar.model.domain.withInteractions
import com.gaoyun.roar.presentation.BackNavigationEffect
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.pet_screen.PetScreenContract
import com.gaoyun.roar.presentation.pet_screen.PetScreenViewModel
import com.gaoyun.roar.ui.theme.RoarThemePreview
import com.gaoyun.roar.util.SharedDateUtils
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.getViewModel

@Composable
fun PetScreenDestination(
    onNavigationCall: (NavigationSideEffect) -> Unit,
    petId: String,
) {
    val viewModel: PetScreenViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_CREATE) {
            viewModel.buildScreenState(petId)
        }
    }

    val showCompleteReminderDateDialog = remember { mutableStateOf(false) }
    val completeReminderDateDialogDate =
        remember { mutableStateOf(SharedDateUtils.currentDateTime()) }
    val reminderToCompleteId = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        viewModel.effect.onEach { effect ->
            when (effect) {
                is PetScreenContract.Effect.Navigation -> onNavigationCall(effect)
                is PetScreenContract.Effect.NavigateBack -> onNavigationCall(BackNavigationEffect)
            }
        }.collect()
    }

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
                contentDescription = stringResource(id = R.string.add_reminder),
                text = stringResource(id = R.string.reminder),
                extended = fabExtended,
                onClick = {
                    viewModel.setEvent(
                        PetScreenContract.Event.AddReminderButtonClicked(
                            state.pet?.id ?: ""
                        )
                    )
                })
        },
        floatingActionButtonPosition = FabPosition.End,
        backHandler = { onNavigationCall(BackNavigationEffect) }
    ) {
        if (state.deletePetDialogShow) {
            RemovePetConfirmationDialog(
                petName = state.pet?.name.toString(),
                onDismiss = viewModel::hideDeletePetDialog,
                onConfirm = {
                    viewModel.setEvent(
                        PetScreenContract.Event.OnDeletePetConfirmed(
                            state.pet?.id ?: ""
                        )
                    )
                }
            )
        }

        if (showCompleteReminderDateDialog.value) {
            InteractionCompletionDialog(
                showCompleteReminderDateDialog = showCompleteReminderDateDialog,
                dateTime = completeReminderDateDialogDate.value,
                onConfirmButtonClick = {
                    showCompleteReminderDateDialog.value = false
                    viewModel.setEvent(
                        PetScreenContract.Event.OnInteractionCheckClicked(
                            reminderId = reminderToCompleteId.value ?: "",
                            completed = true,
                            completionDateTime = SharedDateUtils.currentDateAt(
                                hour = completeReminderDateDialogDate.value.hour,
                                minute = completeReminderDateDialogDate.value.minute,
                            ),
                        )
                    )
                },
                onDismissButtonClick = {
                    showCompleteReminderDateDialog.value = false
                    viewModel.setEvent(
                        PetScreenContract.Event.OnInteractionCheckClicked(
                            reminderId = reminderToCompleteId.value ?: "",
                            completed = true,
                            completionDateTime = completeReminderDateDialogDate.value,
                        )
                    )
                }
            )
        }

        BoxWithLoader(isLoading = state.isLoading) {
            state.pet?.let { pet ->
                PetContainer(
                    pet = pet.withInteractions(state.interactions),
                    inactiveInteractions = state.inactiveInteractions,
                    onInteractionClick = {
                        viewModel.setEvent(
                            PetScreenContract.Event.InteractionClicked(
                                it
                            )
                        )
                    },
                    onDeletePetClick = { viewModel.setEvent(PetScreenContract.Event.OnDeletePetClicked) },
                    onEditPetClick = { viewModel.setEvent(PetScreenContract.Event.OnEditPetClick) },
                    onInteractionCheckClicked = { reminderId, completed, completionDateTime ->
                        if (completed) {
                            reminderToCompleteId.value = reminderId
                            completeReminderDateDialogDate.value = completionDateTime
                            showCompleteReminderDateDialog.value = true
                        } else {
                            viewModel.setEvent(
                                PetScreenContract.Event.OnInteractionCheckClicked(
                                    reminderId = reminderId,
                                    completed = false,
                                    completionDateTime = completionDateTime
                                )
                            )
                        }
                    },
                    modifier = Modifier
//                        .verticalScroll(verticalScroll)
                        .padding(start = 8.dp, end = 8.dp)
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