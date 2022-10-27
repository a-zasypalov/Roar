package com.gaoyun.roar.presentation.add_pet

import com.gaoyun.roar.presentation.ViewEvent
import com.gaoyun.roar.presentation.ViewSideEffect
import com.gaoyun.roar.presentation.ViewState

class AddPetScreenContract {

    sealed class Event : ViewEvent {
    }

    data class State(
        val isLoading: Boolean = false
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        object PetAdded : Effect()

        sealed class Navigation : Effect() {
            object NavigateBack : Navigation()
        }
    }
}