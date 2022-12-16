package com.gaoyun.roar.presentation.add_pet.avatar

import com.gaoyun.roar.config.PetsConfig
import com.gaoyun.roar.domain.pet.SetPetAvatar
import com.gaoyun.roar.presentation.BaseViewModel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AddPetAvatarScreenViewModel :
    BaseViewModel<AddPetAvatarScreenContract.Event, AddPetAvatarScreenContract.State, AddPetAvatarScreenContract.Effect>(),
    KoinComponent {

    private val setAvatar: SetPetAvatar by inject()

    override fun setInitialState() = AddPetAvatarScreenContract.State(avatars = listOf())

    override fun handleEvents(event: AddPetAvatarScreenContract.Event) {
        when (event) {
            is AddPetAvatarScreenContract.Event.AvatarChosen -> {
                val petId = viewState.value.petId
                if (petId != null) {
                    savePetAvatar(petId, event.avatar)
                } else {
                    setEffect { AddPetAvatarScreenContract.Effect.Navigation.ToPetData(event.avatar) }
                }
            }
        }
    }

    fun petTypeChosen(petType: String, petId: String?) = setState {
        AddPetAvatarScreenContract.State(avatars = PetsConfig.petAvatars(petType), petId = petId)
    }

    private fun savePetAvatar(petId: String, avatar: String) = scope.launch {
        setAvatar.setAvatar(petId, avatar).collect {
            setEffect { AddPetAvatarScreenContract.Effect.Navigation.NavigateBack }
        }
    }
}