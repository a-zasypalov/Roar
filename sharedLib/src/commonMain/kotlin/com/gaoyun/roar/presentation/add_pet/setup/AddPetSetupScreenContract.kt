package com.gaoyun.roar.presentation.add_pet.setup

import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.ViewEvent
import com.gaoyun.roar.presentation.ViewSideEffect
import com.gaoyun.roar.presentation.ViewState

class AddPetSetupScreenContract {
    sealed class Event : ViewEvent {
        class PetInit(val petId: String) : Event()
        data object ContinueButtonClicked : Event()
        data object OpenTemplatesButtonClicked : Event()
    }

    data class State(
        val pet: Pet? = null,
        val isLoading: Boolean = false,
        val isComplete: Boolean = false
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect(), NavigationSideEffect {
            object Continue : Navigation()
            data class OpenTemplates(val petId: String) : Navigation()
        }
    }
}