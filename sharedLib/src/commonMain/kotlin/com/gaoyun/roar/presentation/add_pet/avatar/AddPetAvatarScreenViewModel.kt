package com.gaoyun.roar.presentation.add_pet.avatar

import com.gaoyun.roar.config.PetsConfig
import com.gaoyun.roar.presentation.BaseViewModel
import org.koin.core.component.KoinComponent

class AddPetAvatarScreenViewModel :
    BaseViewModel<AddPetAvatarScreenContract.Event, AddPetAvatarScreenContract.State, AddPetAvatarScreenContract.Effect>(),
    KoinComponent {

    override fun setInitialState() = AddPetAvatarScreenContract.State(avatars = listOf())

    override fun handleEvents(event: AddPetAvatarScreenContract.Event) {
        when (event) {
            is AddPetAvatarScreenContract.Event.AvatarChosen -> setEffect {
                AddPetAvatarScreenContract.Effect.Navigation.ToPetData(event.avatar)
            }
        }
    }

    fun petTypeChosen(petType: String) = setState {
        AddPetAvatarScreenContract.State(avatars = PetsConfig.petAvatars(petType))
    }
}