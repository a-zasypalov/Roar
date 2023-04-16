package com.gaoyun.feature_pet_screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.R
import com.gaoyun.common.composables.*
import com.gaoyun.common.dialog.InteractionCompletionDialog
import com.gaoyun.common.theme.RoarTheme
import com.gaoyun.feature_pet_screen.view.PetContainer
import com.gaoyun.roar.model.domain.*
import com.gaoyun.roar.model.domain.interactions.InteractionGroup
import com.gaoyun.roar.model.domain.interactions.InteractionRemindConfig
import com.gaoyun.roar.model.domain.interactions.InteractionType
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.presentation.BackNavigationEffect
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.pet_screen.PetScreenContract
import com.gaoyun.roar.presentation.pet_screen.PetScreenViewModel
import com.gaoyun.roar.util.toLocalDate
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.*
import org.koin.androidx.compose.getViewModel
import java.time.LocalDateTime
import kotlin.time.Duration.Companion.hours

@OptIn(ExperimentalMaterial3Api::class)
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
    val completeReminderDateDialogDate = remember { mutableStateOf<LocalDateTime>(LocalDateTime.now()) }
    val reminderToCompleteId = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        viewModel.effect.onEach { effect ->
            when (effect) {
                is PetScreenContract.Effect.Navigation -> onNavigationCall(effect)
                is PetScreenContract.Effect.NavigateBack -> onNavigationCall(BackNavigationEffect)
            }
        }.collect()
    }

    SurfaceScaffold(
        floatingActionButton = {
            RoarExtendedFAB(
                icon = Icons.Filled.Add,
                contentDescription = stringResource(id = R.string.add_reminder),
                text = stringResource(id = R.string.reminder),
                onClick = { viewModel.setEvent(PetScreenContract.Event.AddReminderButtonClicked(state.pet?.id ?: "")) })
        },
        floatingActionButtonPosition = FabPosition.End,
        backHandler = { onNavigationCall(BackNavigationEffect) }
    ) {
        if (state.deletePetDialogShow) {
            RemovePetConfirmationDialog(
                petName = state.pet?.name.toString(),
                onDismiss = viewModel::hideDeletePetDialog,
                onConfirm = { viewModel.setEvent(PetScreenContract.Event.OnDeletePetConfirmed(state.pet?.id ?: "")) }
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
                    viewModel.setEvent(
                        PetScreenContract.Event.OnInteractionCheckClicked(
                            reminderId = reminderToCompleteId.value ?: "",
                            completed = true,
                            completionDateTime = currentDateTime.toKotlinLocalDateTime(),
                        )
                    )
                },
                onDismissButtonClick = {
                    showCompleteReminderDateDialog.value = false
                    viewModel.setEvent(
                        PetScreenContract.Event.OnInteractionCheckClicked(
                            reminderId = reminderToCompleteId.value ?: "",
                            completed = true,
                            completionDateTime = completeReminderDateDialogDate.value.toKotlinLocalDateTime(),
                        )
                    )
                }
            )
        }

        BoxWithLoader(isLoading = state.isLoading) {
            state.pet?.let { pet ->
                PetContainer(
                    pet = pet.withInteractions(state.interactions),
                    showLastReminder = state.showLastReminder,
                    onInteractionClick = { viewModel.setEvent(PetScreenContract.Event.InteractionClicked(it)) },
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
                                    reminderId,
                                    false,
                                    completionDateTime.toKotlinLocalDateTime()
                                )
                            )
                        }
                    },
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(start = 8.dp, end = 8.dp)
                        .fillMaxWidth()
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun PetScreenPreview() {
    RoarTheme {
        SurfaceScaffold {
            PetContainer(
                Pet(
                    petType = PetType.CAT,
                    breed = "Colorpoint Shorthair",
                    name = "Senior Android Developer",
                    avatar = "ic_cat_15",
                    userId = "123",
                    birthday = Clock.System.now().toLocalDate(),
                    isSterilized = false,
                    gender = Gender.MALE,
                    chipNumber = "123123456456",
                    dateCreated = Clock.System.now().toLocalDate()
                ).withInteractions(
                    mapOf(
                        InteractionGroup.CARE to
                                listOf(
                                    InteractionWithReminders(
                                        petId = "",
                                        type = InteractionType.CUSTOM,
                                        name = "Interaction Name",
                                        group = InteractionGroup.CARE,
                                        isActive = true,
                                        remindConfig = InteractionRemindConfig(),
                                        reminders = listOf(
                                            Reminder(
                                                interactionId = "",
                                                dateTime = Clock.System.now().plus(1.hours).toLocalDateTime(TimeZone.currentSystemDefault())
                                            )
                                        )
                                    )
                                )
                    )
                ),
                true,
                {}, {}, {}, { _, _, _ -> }
            )
        }
    }
}