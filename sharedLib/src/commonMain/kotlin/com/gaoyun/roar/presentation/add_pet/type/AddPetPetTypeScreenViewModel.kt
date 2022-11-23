package com.gaoyun.roar.presentation.add_pet.type

import com.gaoyun.roar.config.PetsConfig
import com.gaoyun.roar.presentation.BaseViewModel
import org.koin.core.component.KoinComponent

class AddPetPetTypeScreenViewModel :
    BaseViewModel<AddPetPetTypeScreenContract.Event, AddPetPetTypeScreenContract.State, AddPetPetTypeScreenContract.Effect>(),
    KoinComponent {

    override fun setInitialState() = AddPetPetTypeScreenContract.State(petTypes = PetsConfig.petTypes)

    override fun handleEvents(event: AddPetPetTypeScreenContract.Event) {
        when (event) {
            is AddPetPetTypeScreenContract.Event.PetTypeChosen -> setEffect {
                AddPetPetTypeScreenContract.Effect.Navigation.ToPetAvatar(event.petType.toString())
            }
        }
    }
}