package com.gaoyun.feature_home_screen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.R
import com.gaoyun.common.composables.BoxWithLoader
import com.gaoyun.common.composables.RoarExtendedFAB
import com.gaoyun.common.composables.Spacer
import com.gaoyun.common.composables.SurfaceScaffold
import com.gaoyun.common.dialog.InteractionCompletionDialog
import com.gaoyun.feature_home_screen.states.HomeState
import com.gaoyun.feature_home_screen.states.NoPetsState
import com.gaoyun.feature_home_screen.states.NoUserState
import com.gaoyun.feature_home_screen.view.InteractionPetChooser
import com.gaoyun.feature_pet_screen.RemovePetConfirmationDialog
import com.gaoyun.roar.model.domain.PetWithInteractions
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.home_screen.HomeScreenContract
import com.gaoyun.roar.presentation.home_screen.HomeScreenViewModel
import com.gaoyun.roar.util.SharedDateUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.getViewModel

@Composable
fun HomeScreenDestination(onNavigationCall: (NavigationSideEffect) -> Unit) {
    val viewModel: HomeScreenViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value
    val activity = LocalContext.current as AppCompatActivity

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            viewModel.checkUserRegistered()
        }
    }

    val showCompleteReminderDateDialog = remember { mutableStateOf(false) }
    val completeReminderDateDialogDate = remember { mutableStateOf(SharedDateUtils.currentDateTime()) }
    val reminderToCompleteId = remember { mutableStateOf<String?>(null) }
    val petToComplete = remember { mutableStateOf<PetWithInteractions?>(null) }

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        viewModel.effect.onEach { effect ->
            when (effect) {
                is HomeScreenContract.Effect.NavigateBack -> activity.finish()
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

    BackHandler { activity.finish() }

    SurfaceScaffold(
        floatingActionButton = {
            if (state.pets.isNotEmpty()) {
                RoarExtendedFAB(
                    icon = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.add_reminder),
                    text = stringResource(id = R.string.reminder),
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
        val signInLauncher = rememberLauncherForActivityResult(
            FirebaseAuthUIActivityResultContract()
        ) { res ->
            if (res.resultCode == Activity.RESULT_OK) {
                Firebase.auth.currentUser?.let { user ->
                    viewModel.setEvent(HomeScreenContract.Event.LoginUser(user.uid))
                }
            }
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
                        state = verticalScroll
                    )
                } else {
                    NoPetsState(userName = user.name,
                        onAddPetButtonClick = viewModel::openAddPetScreen,
                        onUserDetailsClick = { viewModel.setEvent(HomeScreenContract.Event.ToUserScreenClicked) }
                    )
                }
            } ?: if (!state.isLoading) {
                NoUserState(onRegisterButtonClick = viewModel::openRegistration,
                    onLoginButtonClick = {
                        signInLauncher.launch(
                            AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build()))
                                .setLogo(R.drawable.ic_tab_home)
                                .setTheme(R.style.RoarTheme)
                                .setIsSmartLockEnabled(false)
                                .build()
                        )
                    })
            } else Spacer(size = 1.dp)
        }
    }
}