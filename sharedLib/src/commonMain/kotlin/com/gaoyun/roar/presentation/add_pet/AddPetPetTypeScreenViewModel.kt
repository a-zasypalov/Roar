package com.gaoyun.roar.presentation.add_pet

import com.gaoyun.roar.config.PetsConfig
import com.gaoyun.roar.presentation.BaseViewModel
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