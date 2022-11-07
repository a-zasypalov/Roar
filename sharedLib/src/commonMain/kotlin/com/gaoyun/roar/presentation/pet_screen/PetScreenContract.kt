package com.gaoyun.roar.presentation.pet_screen

import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.interactions.InteractionTemplate
import com.gaoyun.roar.presentation.ViewEvent
import com.gaoyun.roar.presentation.ViewSideEffect
import com.gaoyun.roar.presentation.ViewState

class PetScreenContract {

    sealed class Event : ViewEvent {
        class AddReminderButtonClicked(val petId: String) : Event()
    }

    data class State(
        val isLoading: Boolean = false,
        val pet: Pet? = null,
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect() {
            class ToInteractionTemplates(val petId: String) : Navigation()
            object NavigateBack : Navigation()
        }
    }
}