package com.gaoyun.roar.presentation.add_pet.setup

import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.presentation.BaseViewModel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AddPetSetupScreenViewModel :
    BaseViewModel<AddPetSetupScreenContract.Event, AddPetSetupScreenContract.State, AddPetSetupScreenContract.Effect>(),
    KoinComponent {

    private val getPetUseCase: GetPetUseCase by inject()

    override fun setInitialState() = AddPetSetupScreenContract.State(isLoading = false)

    override fun handleEvents(event: AddPetSetupScreenContract.Event) {
        when (event) {
            is AddPetSetupScreenContract.Event.PetInit -> getPet(event.petId)
            is AddPetSetupScreenContract.Event.ContinueButtonClicked -> setEffect {
                AddPetSetupScreenContract.Effect.Continue
            }
        }
    }

    private fun getPet(petId: String) = scope.launch {
        getPetUseCase.getPet(petId).collect { pet ->
            setState { copy(pet = pet) }
        }
    }
}