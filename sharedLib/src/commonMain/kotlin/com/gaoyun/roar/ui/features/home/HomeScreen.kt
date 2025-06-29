package com.gaoyun.roar.ui.features.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.gaoyun.roar.model.domain.withoutInteractions
import com.gaoyun.roar.ui.common.composables.BoxWithLoader
import com.gaoyun.roar.ui.common.composables.RoarExtendedFAB
import com.gaoyun.roar.ui.common.composables.SurfaceScaffold
import com.gaoyun.roar.ui.common.dialog.InteractionCompletionDialog
import com.gaoyun.roar.ui.common.dialog.RemovePetConfirmationDialog
import com.gaoyun.roar.ui.navigation.ToAddPet
import com.gaoyun.roar.ui.navigation.ToAddReminder
import com.gaoyun.roar.ui.navigation.ToEditPet
import com.gaoyun.roar.ui.navigation.ToInteractionDetails
import com.gaoyun.roar.ui.navigation.ToPetScreen
import com.gaoyun.roar.ui.navigation.ToUserRegistration
import com.gaoyun.roar.ui.navigation.ToUserScreen
import com.gaoyun.roar.ui.features.home.states.HomeState
import com.gaoyun.roar.ui.features.home.states.NoPetsState
import com.gaoyun.roar.ui.features.home.states.NoUserState
import com.gaoyun.roar.ui.features.home.view.InteractionPetChooser
import com.gaoyun.roar.ui.features.registration.RegistrationLauncherComposable
import com.gaoyun.roar.ui.navigation.NavigationSideEffect
import com.gaoyun.roar.util.Platform
import com.gaoyun.roar.util.PlatformNames
import com.gaoyun.roar.util.SharedDateUtils
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.add_reminder
import roar.sharedlib.generated.resources.reminder

@Composable
fun HomeScreenDestination(onNavigationCall: (NavigationSideEffect) -> Unit) {
    val viewModel = koinViewModel<HomeScreenViewModel>()
    val state = viewModel.viewState.collectAsState().value

    val showCompleteReminderDateDialog = remember { mutableStateOf(false) }
    val completeReminderDateDialogDate = remember { mutableStateOf(SharedDateUtils.currentDateTime()) }
    val reminderToCompleteId = remember { mutableStateOf<String?>(null) }
    val petToComplete = remember { mutableStateOf<PetWithInteractions?>(null) }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.checkUserRegistered {
            onNavigationCall(ToUserRegistration)
        }
    }

    val verticalScroll = rememberLazyListState()
    var fabExtended by remember { mutableStateOf(true) }
    LaunchedEffect(verticalScroll) {
        var prev = 0
        snapshotFlow { verticalScroll.firstVisibleItemIndex }
            .collect {
                fabExtended = it <= prev
                prev = it
            }
    }

//    BackHandler { onNavigationCall(CloseAppNavigationSideEffect) }

    SurfaceScaffold(
        floatingActionButton = {
            if (state.pets.isNotEmpty()) {
                RoarExtendedFAB(
                    icon = Icons.Filled.Add,
                    contentDescription = stringResource(Res.string.add_reminder),
                    text = stringResource(Res.string.reminder),
                    extended = fabExtended,
                    onClick = {
                        if (state.pets.size > 1) {
                            viewModel.setPetChooserShow(true)
                        } else {
                            onNavigationCall(ToAddReminder(state.pets.firstOrNull()?.id ?: ""))
                        }
                    })
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {

        val registrationCallback: (String, String) -> Unit = { _, id ->
            viewModel.onLoginUser(id) { onNavigationCall(ToUserRegistration) }
        }

        val registrationLauncher = when (Platform.name) {
            PlatformNames.Android -> (viewModel.registrationLauncher as? RegistrationLauncherComposable)?.launcherComposable(registrationCallback)
            PlatformNames.IOS -> viewModel.registrationLauncher.launcher(registrationCallback)
        }

        when {
            state.showPetChooser -> {
                InteractionPetChooser(
                    pets = state.pets,
                    onPetChosen = { onNavigationCall(ToAddReminder(it)) },
                    onDismiss = { viewModel.setPetChooserShow(false) }
                )
            }

            showCompleteReminderDateDialog.value -> {
                InteractionCompletionDialog(
                    showCompleteReminderDateDialog = showCompleteReminderDateDialog,
                    dateTime = completeReminderDateDialogDate.value,
                    onConfirmButtonClick = {
                        showCompleteReminderDateDialog.value = false
                        petToComplete.value?.let { pet ->
                            viewModel.markReminderComplete(
                                pet = pet,
                                reminderId = reminderToCompleteId.value ?: "",
                                completed = true,
                                completionDateTime = SharedDateUtils.currentDateAt(
                                    hour = completeReminderDateDialogDate.value.hour,
                                    minute = completeReminderDateDialogDate.value.minute
                                )
                            )
                        }
                    },
                    onDismissButtonClick = {
                        showCompleteReminderDateDialog.value = false
                        petToComplete.value?.let { pet ->
                            viewModel.markReminderComplete(
                                pet = pet,
                                reminderId = reminderToCompleteId.value ?: "",
                                completed = true,
                                completionDateTime = completeReminderDateDialogDate.value
                            )
                        }
                    }
                )
            }

            state.deletePetDialogShow -> {
                RemovePetConfirmationDialog(
                    petName = state.pets.firstOrNull()?.name.orEmpty(),
                    onDismiss = viewModel::hideDeletePetDialog,
                    onConfirm = {
                        state.pets.firstOrNull()?.let { pet ->
                            viewModel.onDeletePetConfirmed(pet) {
                                onNavigationCall(ToUserRegistration)
                            }
                        }
                    }
                )
            }
        }

        BoxWithLoader(isLoading = state.isLoading) {
            state.user?.let { user ->
                if (state.pets.isNotEmpty()) {
                    HomeState(
                        screenModeFull = state.screenModeFull,
                        showCustomizationPrompt = state.showCustomizationPrompt,
                        pets = state.pets,
                        inactiveInteractions = state.inactiveInteractions,
                        onAddPetButtonClick = { onNavigationCall(ToAddPet) },
                        onPetCardClick = { petId -> onNavigationCall(ToPetScreen(petId)) },
                        onInteractionClick = { interactionClicked ->
                            onNavigationCall(ToInteractionDetails(interactionClicked.interactionId))
                        },
                        onDeletePetClick = { viewModel.onDeletePetClicked() },
                        onEditPetClick = { pet -> onNavigationCall(ToEditPet(pet.pet.withoutInteractions())) },
                        onInteractionCheckClicked = { pet, reminderId, completed, completionDateTime ->
                            if (completed) {
                                petToComplete.value = pet
                                reminderToCompleteId.value = reminderId
                                completeReminderDateDialogDate.value = completionDateTime
                                showCompleteReminderDateDialog.value = true
                            } else {
                                viewModel.markReminderComplete(pet, reminderId, false, completionDateTime)
                            }
                        },
                        onUserDetailsClick = { onNavigationCall(ToUserScreen) },
                        onClosePromptClick = { viewModel.closeCustomizationPrompt() },
                        state = verticalScroll
                    )
                } else {
                    NoPetsState(
                        userName = user.name,
                        onAddPetButtonClick = { onNavigationCall(ToAddPet) },
                        onUserDetailsClick = { onNavigationCall(ToUserScreen) }
                    )
                }
            } ?: if (!state.isLoading) {
                NoUserState(
                    onRegisterButtonClick = { onNavigationCall(ToUserRegistration) },
                    onLoginButtonClick = { registrationLauncher?.invoke() }
                )
            } else Spacer(Modifier.size(1.dp))
        }
    }
}