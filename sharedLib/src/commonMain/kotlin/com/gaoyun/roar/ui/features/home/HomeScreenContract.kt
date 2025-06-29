package com.gaoyun.roar.ui.features.home

import com.gaoyun.roar.model.domain.PetWithInteractions
import com.gaoyun.roar.ui.navigation.NavigationSideEffect

class HomeScreenContract {

    sealed class Event {
        class InteractionClicked(val petId: String, val interactionId: String) : Event()
        class OnDeletePetClicked(val pet: PetWithInteractions) : Event()
        class ToEditPetClicked(val pet: PetWithInteractions) : Event()
    }

    sealed class Effect {
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