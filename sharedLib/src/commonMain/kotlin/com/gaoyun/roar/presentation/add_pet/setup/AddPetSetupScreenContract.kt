package com.gaoyun.roar.presentation.add_pet.setup

import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.presentation.ViewEvent
import com.gaoyun.roar.presentation.ViewSideEffect
import com.gaoyun.roar.presentation.ViewState

class AddPetSetupScreenContract {
    sealed class Event : ViewEvent {
        class PetInit(val petId: String) : Event()
        object ContinueButtonClicked : Event()
    }

    data class State(
        val pet: Pet? = null,
        val isLoading: Boolean = false
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        object Continue : Effect()

        sealed class Navigation : Effect() {
            object NavigateBack : Navigation()
            object AddingComplete : Navigation()
        }
    }
}