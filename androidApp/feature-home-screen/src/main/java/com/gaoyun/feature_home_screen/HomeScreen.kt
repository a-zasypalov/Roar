package com.gaoyun.feature_home_screen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import org.koin.androidx.compose.getViewModel
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
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
    val completeReminderDateDialogDate = remember { mutableStateOf<LocalDateTime>(LocalDateTime.now()) }
    val reminderToCompleteId = remember { mutableStateOf<String?>(null) }
    val petToComplete = remember { mutableStateOf<PetWithInteractions?>(null) }

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        viewModel.effect.onEach { effect ->
            when (effect) {
                is HomeScreenContract.Effect.Navigation -> onNavigationCall(effect)
                is HomeScreenContract.Effect.NavigateBack -> activity.finish()
            }
        }.collect()
    }

    BackHandler { activity.finish() }

    SurfaceScaffold(
        floatingActionButton = {
            if (state.pets.isNotEmpty()) {
                RoarExtendedFAB(
                    icon = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.add_reminder),
                    text = stringResource(id = R.string.reminder),
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
        if (state.showPetChooser) {
            Dialog(
                onDismissRequest = { viewModel.setEvent(HomeScreenContract.Event.SetPetChooserShow(false)) }
            ) {
                InteractionPetChooser(
                    pets = state.pets,
                    onPetChosen = { viewModel.setEvent(HomeScreenContract.Event.PetChosenForReminderCreation(it)) }
                )
            }
        }

        if (showCompleteReminderDateDialog.value) {
            var currentDateTime = LocalDateTime.now().withHour(completeReminderDateDialogDate.value.hour)
            currentDateTime = currentDateTime.withMinute(completeReminderDateDialogDate.value.minute)

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
                                completionDateTime = currentDateTime.toKotlinLocalDateTime(),
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
                                completionDateTime = completeReminderDateDialogDate.value.toKotlinLocalDateTime(),
                                pet = pet
                            )
                        )
                    }
                }
            )
        }

        if (state.deletePetDialogShow) {
            RemovePetConfirmationDialog(
                petName = state.pets.first().name,
                onDismiss = viewModel::hideDeletePetDialog,
                onConfirm = { viewModel.setEvent(HomeScreenContract.Event.OnDeletePetConfirmed(state.pets.first())) }
            )
        }

        val signInLauncher = rememberLauncherForActivityResult(
            FirebaseAuthUIActivityResultContract()
        ) { res ->
            if (res.resultCode == Activity.RESULT_OK) {
                Firebase.auth.currentUser?.let { user ->
                    viewModel.setEvent(HomeScreenContract.Event.LoginUser(user.uid))
                }
            }
        }

        BoxWithLoader(isLoading = state.isLoading) {
            state.user?.let { user ->
                if (state.pets.isNotEmpty()) {
                    HomeState(
                        pets = state.pets,
                        showLastReminder = state.showLastReminder,
                        remindersPerPet = state.remindersPerPet,
                        onAddPetButtonClick = viewModel::openAddPetScreen,
                        onPetCardClick = viewModel::openPetScreen,
                        onInteractionClick = { petId, interactionId -> viewModel.setEvent(HomeScreenContract.Event.InteractionClicked(petId, interactionId)) },
                        onDeletePetClick = { pet -> viewModel.setEvent(HomeScreenContract.Event.OnDeletePetClicked(pet)) },
                        onEditPetClick = { pet -> viewModel.setEvent(HomeScreenContract.Event.ToEditPetClicked(pet = pet)) },
                        onInteractionCheckClicked = { pet, reminderId, completed, completionDateTime ->
                            if (completed) {
                                petToComplete.value = pet
                                reminderToCompleteId.value = reminderId
                                completeReminderDateDialogDate.value = completionDateTime.toJavaLocalDateTime()
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
                        onUserDetailsClick = { viewModel.setEvent(HomeScreenContract.Event.ToUserScreenClicked) }
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