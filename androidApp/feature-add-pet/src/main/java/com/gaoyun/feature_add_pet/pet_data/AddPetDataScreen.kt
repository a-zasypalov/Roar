package com.gaoyun.feature_add_pet.pet_data

import androidx.activity.compose.BackHandler
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.Lifecycle
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.composables.BoxWithLoader
import com.gaoyun.roar.ui.SurfaceScaffold
import com.gaoyun.roar.presentation.BackNavigationEffect
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.add_pet.data.AddPetDataScreenContract
import com.gaoyun.roar.presentation.add_pet.data.AddPetDataScreenViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.getViewModel
import com.gaoyun.common.R as CommonR


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
    val context = LocalContext.current

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            viewModel.setEvent(AddPetDataScreenContract.Event.PetDataInit(petType, avatar, petId, Locale.current.language, context.getString(CommonR.string.no_breed)))
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