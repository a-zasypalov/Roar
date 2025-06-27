package com.gaoyun.roar.presentation.add_pet.setup

import com.gaoyun.roar.ui.navigation.NavigationSideEffect

class AddPetSetupScreenContract {
    sealed class Event {
        data object ContinueButtonClicked : Event()
        data object OpenTemplatesButtonClicked : Event()
    }

    sealed class Effect {
        sealed class Navigation : Effect(), NavigationSideEffect {
            object Continue : Navigation()
            data class OpenTemplates(val petId: String) : Navigation()
        }
    }
}