package com.gaoyun.roar.ui.features.add_pet.pet_data

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.gaoyun.roar.ui.common.composables.BoxWithLoader
import com.gaoyun.roar.ui.common.composables.SurfaceScaffold
import com.gaoyun.roar.ui.features.add_pet.pet_data.composables.AddPetForm
import com.gaoyun.roar.ui.navigation.BackNavigationEffect
import com.gaoyun.roar.ui.navigation.NavigationSideEffect
import com.gaoyun.roar.ui.navigation.ToAvatarEdit
import com.gaoyun.roar.ui.navigation.ToPetSetup
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.no_breed

@Composable
fun AddPetDataDestination(
    navigate: (NavigationSideEffect) -> Unit,
    petType: String,
    avatar: String,
    petId: String? = null
) {
    val viewModel = koinViewModel<AddPetDataScreenViewModel>()
    val state = viewModel.viewState.collectAsState().value
    val snackbarHostState = remember { SnackbarHostState() }

    val noBreedString = stringResource(Res.string.no_breed)

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.initialize(
            petType,
            avatar,
            petId,
            Locale.current.language,
            noBreedString
        )
    }

    SurfaceScaffold(
        backHandler = { handleBack(viewModel, state, navigate, avatar) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) {
        BoxWithLoader(isLoading = state.isLoading) {
            if (petId == null || state.pet != null) {
                AddPetForm(
                    petBreeds = state.breeds,
                    petType = petType,
                    onRegisterClick = { form ->
                        viewModel.addOrEditPet(
                            petType = petType,
                            breed = form.breed,
                            name = form.name,
                            avatar = form.avatar,
                            birthday = form.birthday,
                            gender = form.gender,
                            chipNumber = form.chipNumber,
                            isSterilized = form.isSterilized,
                            onSuccessNavigate = { newId ->
                                navigate(ToPetSetup(newId))
                            },
                            onBack = {
                                navigate(BackNavigationEffect)
                            }
                        )
                    },
                    avatar = state.pet?.avatar ?: avatar,
                    petToEdit = state.pet,
                    snackbarHostState = snackbarHostState,
                    onAvatarEditClick = { pet ->
                        navigate(ToAvatarEdit(pet.petId, pet.petType))
                    }
                )
            }
        }
    }
}

private fun handleBack(
    viewModel: AddPetDataScreenViewModel,
    state: AddPetDataScreenContractState,
    navigate: (NavigationSideEffect) -> Unit,
    originalAvatar: String
) {
    val currentAvatar = state.pet?.avatar
    val petId = state.pet?.id
    if (currentAvatar == null || currentAvatar == originalAvatar || petId == null) {
        navigate(BackNavigationEffect)
    } else {
        viewModel.revertPetAvatar(
            petId,
            originalAvatar,
            onBack = {
                navigate(BackNavigationEffect)
            }
        )
    }
}