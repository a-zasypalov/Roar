package com.gaoyun.roar.ui.features.add_pet.pet_data

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.text.intl.Locale
import com.gaoyun.roar.presentation.BackNavigationEffect
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.add_pet.data.AddPetDataScreenContract
import com.gaoyun.roar.presentation.add_pet.data.AddPetDataScreenViewModel
import com.gaoyun.roar.ui.common.composables.SurfaceScaffold
import com.gaoyun.roar.ui.common.composables.BoxWithLoader
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.BackHandler


@Composable
fun AddPetDataDestination(
    onNavigationCall: (NavigationSideEffect) -> Unit,
    petType: String,
    avatar: String,
    petId: String? = null
) {
    val viewModel: AddPetDataScreenViewModel = koinViewModel(vmClass = AddPetDataScreenViewModel::class)
    val state = viewModel.viewState.collectAsState().value
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        //TODO: Was on onResume
        viewModel.setEvent(
            AddPetDataScreenContract.Event.PetDataInit(
                petType,
                avatar,
                petId,
                Locale.current.language,
                "No breed", //context.getString(CommonR.string.no_breed)
            )
        )
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