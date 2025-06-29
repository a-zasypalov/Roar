package com.gaoyun.roar.ui.features.pet

import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.ui.navigation.NavigationSideEffect

class PetScreenContract {
    sealed class Effect {
        data object NavigateBack : Effect()
        sealed class Navigation : Effect(), NavigationSideEffect {
            class ToInteractionDetails(val interactionId: String) : Navigation()
            class ToInteractionTemplates(val petId: String) : Navigation()
            class ToEditPet(val pet: Pet) : Navigation()
        }
    }
}