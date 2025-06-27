package com.gaoyun.roar.presentation.add_pet

import androidx.lifecycle.viewModelScope
import com.gaoyun.roar.config.PetsConfig
import com.gaoyun.roar.domain.pet.SetPetAvatar
import com.gaoyun.roar.presentation.BaseViewModel
import com.gaoyun.roar.ui.navigation.NavigationSideEffect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AddPetAvatarScreenViewModel(
    private val setAvatar: SetPetAvatar,
) : BaseViewModel() {

    override val viewState = MutableStateFlow(AddPetAvatarScreenContractState(avatars = listOf()))

    fun petTypeChosen(petType: String, petId: String?) {
        viewState.value = AddPetAvatarScreenContractState(avatars = PetsConfig.petAvatars(petType), petId = petId)
    }

    fun savePetAvatar(petId: String, avatar: String) = viewModelScope.launch {
        setAvatar.setAvatar(petId, avatar)
    }
}

class ToPetData(val avatar: String, val petType: String) : NavigationSideEffect

data class AddPetAvatarScreenContractState(
    val avatars: List<PetsConfig.PetAvatarConfig>,
    val petId: String? = null,
)