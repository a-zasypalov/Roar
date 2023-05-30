package com.gaoyun.feature_add_pet.pet_data

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.Lifecycle
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.composables.*
import com.gaoyun.roar.presentation.BackNavigationEffect
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.add_pet.data.AddPetDataScreenContract
import com.gaoyun.roar.presentation.add_pet.data.AddPetDataScreenViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.getViewModel


@Composable
fun AddPetDataDestination(
    onNavigationCall: (NavigationSideEffect) -> Unit,
    petType: String,
    avatar: String,
    petId: String? = null
) {
    val viewModel: AddPetDataScreenViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value
    val snackbarHostState = remember { SnackbarHostState() }

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            viewModel.setEvent(AddPetDataScreenContract.Event.PetDataInit(petType, avatar, petId, Locale.current.language))
        }
    }

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        viewModel.effect.onEach { effect ->
            when (effect) {
                is AddPetDataScreenContract.Effect.NavigateBack -> {
                    if (effect.confirmed || state.pet?.avatar == null || state.pet?.avatar == avatar) {
                        onNavigationCall(BackNavigationEffect)
                    } else {
                        viewModel.revertPetAvatar(state.pet?.id ?: "", avatar)
                    }
                }
                is AddPetDataScreenContract.Effect.Navigation -> onNavigationCall(effect)
            }
        }.collect()
    }

    BackHandler(enabled = true) {
        viewModel.setEvent(AddPetDataScreenContract.Event.NavigateBack)
    }

    SurfaceScaffold(
        backHandler = { viewModel.setEvent(AddPetDataScreenContract.Event.NavigateBack) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) {
        BoxWithLoader(isLoading = state.isLoading) {
            if (petId == null || state.pet != null) {
                AddPetForm(
                    petBreeds = state.breeds,
                    petType = petType,
                    onRegisterClick = viewModel::setEvent,
                    avatar = state.pet?.avatar ?: avatar,
                    petToEdit = state.pet,
                    snackbarHostState = snackbarHostState,
                    onAvatarEditClick = viewModel::setEvent,
                )
            }
        }
    }
}