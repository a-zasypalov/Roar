package com.gaoyun.roar.presentation.home_screen

import com.gaoyun.roar.model.domain.PetWithInteractions
import com.gaoyun.roar.ui.navigation.NavigationSideEffect

class HomeScreenContract {

    sealed class Event {
        class InteractionClicked(val petId: String, val interactionId: String) : Event()
        class OnDeletePetClicked(val pet: PetWithInteractions) : Event()
        class ToEditPetClicked(val pet: PetWithInteractions) : Event()
    }

    sealed class Effect {
        data object NavigateBack : Effect()
        sealed class Navigation : Effect(), NavigationSideEffect {
            object ToUserRegistration : Navigation()
            object ToAddPet : Navigation()
            class ToAddReminder(val petId: String) : Navigation()
            class ToPetScreen(val petId: String) : Navigation()
            class ToInteractionDetails(val interactionId: String) : Navigation()
            class ToEditPet(val pet: PetWithInteractions) : Navigation()
            object ToUserScreen : Navigation()
        }
    }
}