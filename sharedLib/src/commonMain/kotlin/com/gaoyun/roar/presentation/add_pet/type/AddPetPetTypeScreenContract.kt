package com.gaoyun.roar.presentation.add_pet.type

import com.gaoyun.roar.config.PetsConfig
import com.gaoyun.roar.model.domain.PetType
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.ViewEvent
import com.gaoyun.roar.presentation.ViewSideEffect
import com.gaoyun.roar.presentation.ViewState

class AddPetPetTypeScreenContract {

    sealed class Event : ViewEvent {
        class PetTypeChosen(val petType: PetType) : Event()
    }

    data class State(
        val petTypes: List<PetsConfig.PetTypeConfig>
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect(), NavigationSideEffect {
            class ToPetAvatar(val petType: String) : Navigation()
        }
    }
}