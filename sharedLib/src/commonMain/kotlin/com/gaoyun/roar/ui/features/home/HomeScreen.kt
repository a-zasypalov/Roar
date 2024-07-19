package com.gaoyun.roar.ui.features.home

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.gaoyun.roar.model.domain.PetWithInteractions
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.home_screen.HomeScreenContract
import com.gaoyun.roar.presentation.home_screen.HomeScreenViewModel
import com.gaoyun.roar.ui.common.composables.BoxWithLoader
import com.gaoyun.roar.ui.common.composables.RoarExtendedFAB
import com.gaoyun.roar.ui.common.composables.Spacer
import com.gaoyun.roar.ui.common.composables.SurfaceScaffold
import com.gaoyun.roar.ui.common.dialog.InteractionCompletionDialog
import com.gaoyun.roar.ui.common.dialog.RemovePetConfirmationDialog
import com.gaoyun.roar.ui.features.home.states.HomeState
import com.gaoyun.roar.ui.features.home.states.NoPetsState
import com.gaoyun.roar.ui.features.home.states.NoUserState
import com.gaoyun.roar.ui.features.home.view.InteractionPetChooser
import com.gaoyun.roar.ui.features.registration.RegistrationLauncherComposable
import com.gaoyun.roar.ui.navigation.CloseAppNavigationSideEffect
import com.gaoyun.roar.util.Platform
import com.gaoyun.roar.util.PlatformNames
import com.gaoyun.roar.util.SharedDateUtils
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.BackHandler
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.add_reminder
import roar.sharedlib.generated.resources.reminder

@Composable
fun HomeScreenDestination(onNavigationCall: (NavigationSideEffect) -> Unit) {
    val viewModel = koinViewModel(vmClass = HomeScreenViewModel::class)
    val state = viewModel.viewState.collectAsState().value

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.checkUserRegistered()
    }

    val showCompleteReminderDateDialog = remember { mutableStateOf(false) }
    val completeReminderDateDialogDate = remember { mutableStateOf(SharedDateUtils.currentDateTime()) }
    val reminderToCompleteId = remember { mutableStateOf<String?>(null) }
    val petToComplete = remember { mutableStateOf<PetWithInteractions?>(null) }

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        viewModel.effect.onEach { effect ->
            when (effect) {
                is HomeScreenContract.Effect.NavigateBack -> {
                    onNavigationCall(CloseAppNavigationSideEffect)
                }

                is HomeScreenContract.Effect.Navigation -> onNavigationCall(effect)
            }
        }.collect()
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

    BackHandler { onNavigationCall(CloseAppNavigationSideEffect) }

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
                            viewModel.setEvent(HomeScreenContract.Event.SetPetChooserShow(true))
                        } else {
                            viewModel.setEvent(HomeScreenContract.Event.PetChosenForReminderCreation(state.pets.firstOrNull()?.id ?: ""))
                        }
                    })
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {

        val registrationCallback = { _: String, id: String -> viewModel.setEvent(HomeScreenContract.Event.LoginUser(id)) }
        val registrationLauncher = when (Platform.name) {
            PlatformNames.Android -> (viewModel.registrationLauncher as? RegistrationLauncherComposable)?.launcherComposable(registrationCallback)
            PlatformNames.IOS -> viewModel.registrationLauncher.launcher(registrationCallback)
        }

        when {
            state.showPetChooser -> {
                InteractionPetChooser(
                    pets = state.pets,
                    onPetChosen = { viewModel.setEvent(HomeScreenContract.Event.PetChosenForReminderCreation(it)) },
                    onDismiss = { viewModel.setEvent(HomeScreenContract.Event.SetPetChooserShow(false)) }
                )
            }

            showCompleteReminderDateDialog.value -> {
                InteractionCompletionDialog(
                    showCompleteReminderDateDialog = showCompleteReminderDateDialog,
                    dateTime = completeReminderDateDialogDate.value,
                    onConfirmButtonClick = {
                        showCompleteReminderDateDialog.value = false
                        petToComplete.value?.let { pet ->
                            viewModel.setEvent(
                                HomeScreenContract.Event.OnInteractionCheckClicked(
                                    reminderId = reminderToCompleteId.value ?: "",
                                    completed = true,
                                    completionDateTime = SharedDateUtils.currentDateAt(
                                        hour = completeReminderDateDialogDate.value.hour,
                                        minute = completeReminderDateDialogDate.value.minute
                                    ),
                                    pet = pet
                                )
                            )
                        }
                    },
                    onDismissButtonClick = {
                        showCompleteReminderDateDialog.value = false
                        petToComplete.value?.let { pet ->
                            viewModel.setEvent(
                                HomeScreenContract.Event.OnInteractionCheckClicked(
                                    reminderId = reminderToCompleteId.value ?: "",
                                    completed = true,
                                    completionDateTime = completeReminderDateDialogDate.value,
                                    pet = pet
                                )
                            )
                        }
                    }
                )
            }

            state.deletePetDialogShow -> {
                RemovePetConfirmationDialog(
                    petName = state.pets.first().name,
                    onDismiss = viewModel::hideDeletePetDialog,
                    onConfirm = { viewModel.setEvent(HomeScreenContract.Event.OnDeletePetConfirmed(state.pets.first())) }
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
                        onAddPetButtonClick = viewModel::openAddPetScreen,
                        onPetCardClick = viewModel::openPetScreen,
                        onInteractionClick = viewModel::setEvent,
                        onDeletePetClick = viewModel::setEvent,
                        onEditPetClick = viewModel::setEvent,
                        onInteractionCheckClicked = { pet, reminderId, completed, completionDateTime ->
                            if (completed) {
                                petToComplete.value = pet
                                reminderToCompleteId.value = reminderId
                                completeReminderDateDialogDate.value = completionDateTime
                                showCompleteReminderDateDialog.value = true
                            } else {
                                viewModel.setEvent(
                                    HomeScreenContract.Event.OnInteractionCheckClicked(
                                        reminderId = reminderId,
                                        completed = false,
                                        completionDateTime = completionDateTime,
                                        pet = pet
                                    )
                                )
                            }
                        },
                        onUserDetailsClick = { viewModel.setEvent(HomeScreenContract.Event.ToUserScreenClicked) },
                        onClosePromptClick = { viewModel.setEvent(HomeScreenContract.Event.RemoveCustomizationPromptClicked) },
                        state = verticalScroll
                    )
                } else {
                    NoPetsState(userName = user.name,
                        onAddPetButtonClick = viewModel::openAddPetScreen,
                        onUserDetailsClick = { viewModel.setEvent(HomeScreenContract.Event.ToUserScreenClicked) }
                    )
                }
            } ?: if (!state.isLoading) {
                NoUserState(
                    onRegisterButtonClick = viewModel::openRegistration,
                    onLoginButtonClick = { registrationLauncher?.invoke() }
                )
            } else Spacer(size = 1.dp)
        }
    }
}