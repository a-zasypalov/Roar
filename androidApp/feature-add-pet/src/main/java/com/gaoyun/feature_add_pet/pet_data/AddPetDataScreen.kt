package com.gaoyun.feature_add_pet

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.gaoyun.common.NavigationKeys
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.composables.*
import com.gaoyun.feature_add_pet.pet_data.AddPetForm
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.add_pet.data.AddPetDataScreenContract
import com.gaoyun.roar.presentation.add_pet.data.AddPetDataScreenViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.getViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetDataDestination(
    navHostController: NavHostController,
    petType: String,
    avatar: String,
    petId: String? = null
) {
    val viewModel: AddPetDataScreenViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            viewModel.setEvent(AddPetDataScreenContract.Event.PetDataInit(petType, avatar, petId))
        }
    }

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        viewModel.effect.onEach { effect ->
            when (effect) {
                is AddPetDataScreenContract.Effect.Navigation.ToPetSetup -> navHostController.navigate(
                    route = "${NavigationKeys.Route.ADD_PET_SETUP}/${effect.petId}"
                )

                is AddPetDataScreenContract.Effect.Navigation.ToAvatarEdit -> navHostController.navigate(
                    route = "${NavigationKeys.Route.EDIT}/${NavigationKeys.Route.AVATAR}/${NavigationKeys.Route.PET_DETAIL}/${effect.petType}/${effect.petId}"
                )

                is AddPetDataScreenContract.Effect.Navigation.NavigateBack -> {
                    if (effect.confirmed || state.pet?.avatar == null || state.pet?.avatar == avatar) {
                        navHostController.navigateUp()
                    } else {
                        viewModel.revertPetAvatar(state.pet?.id ?: "", avatar)
                    }
                }
            }
        }.collect()
    }

    BackHandler(enabled = true) {
        viewModel.setEvent(AddPetDataScreenContract.Event.NavigateBack)
    }

    SurfaceScaffold(
        backHandler = { viewModel.setEvent(AddPetDataScreenContract.Event.NavigateBack) }
    ) {
        BoxWithLoader(isLoading = state.isLoading) {
            if (petId == null || state.pet != null) {
                AddPetForm(
                    petBreeds = state.breeds,
                    petType = petType,
                    onRegisterClick = viewModel::setEvent,
                    avatar = state.pet?.avatar ?: avatar,
                    petToEdit = state.pet,
                    onAvatarEditClick = viewModel::setEvent,
                )
            }
        }
    }
}