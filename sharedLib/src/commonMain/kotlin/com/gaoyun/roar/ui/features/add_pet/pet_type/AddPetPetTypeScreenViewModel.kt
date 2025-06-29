package com.gaoyun.roar.ui.features.add_pet.pet_type

import com.gaoyun.roar.config.PetsConfig
import com.gaoyun.roar.ui.common.BaseViewModel
import com.gaoyun.roar.ui.navigation.NavigationSideEffect
import kotlinx.coroutines.flow.MutableStateFlow

class AddPetPetTypeScreenViewModel : BaseViewModel() {

    override val viewState = MutableStateFlow(
        AddPetPetTypeScreenContractState(petTypes = PetsConfig.petTypes)
    )
}

data class AddPetPetTypeScreenContractState(
    val petTypes: List<PetsConfig.PetTypeConfig>
)

class ToPetAvatar(val petType: String) : NavigationSideEffect