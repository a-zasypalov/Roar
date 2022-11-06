package com.gaoyun.roar.presentation.add_pet.avatar

import com.gaoyun.roar.config.PetsConfig
import com.gaoyun.roar.presentation.ViewEvent
import com.gaoyun.roar.presentation.ViewSideEffect
import com.gaoyun.roar.presentation.ViewState

class AddPetAvatarScreenContract {

    sealed class Event : ViewEvent {
        class PetTypeChosen(val petType: String) : Event()
        class AvatarChosen(val avatar: String) : Event()
    }

    data class State(
        val avatars: List<PetsConfig.PetAvatarConfig>
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        class AvatarChosen(val avatar: String) : Effect()
        sealed class Navigation : Effect() {
            class ToPetData(val avatar: String) : Navigation()
            object NavigateBack : Navigation()
        }
    }
}