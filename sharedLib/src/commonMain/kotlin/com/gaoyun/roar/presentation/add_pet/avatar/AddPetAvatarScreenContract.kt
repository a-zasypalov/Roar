package com.gaoyun.roar.presentation.add_pet.avatar

import com.gaoyun.roar.config.PetsConfig
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.ViewEvent
import com.gaoyun.roar.presentation.ViewSideEffect
import com.gaoyun.roar.presentation.ViewState

class AddPetAvatarScreenContract {

    sealed class Event : ViewEvent {
        class AvatarChosen(val avatar: String, val petType: String) : Event()
    }

    data class State(
        val avatars: List<PetsConfig.PetAvatarConfig>,
        val petId: String? = null,
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        data object NavigateBack : Effect()
        sealed class Navigation : Effect(), NavigationSideEffect {
            class ToPetData(val avatar: String, val petType: String) : Navigation()
        }
    }
}