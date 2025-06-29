package com.gaoyun.roar.ui.features.add_pet.setup

import androidx.lifecycle.viewModelScope
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.ui.common.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddPetSetupScreenViewModel(
    private val getPetUseCase: GetPetUseCase,
) : BaseViewModel() {

    override val viewState = MutableStateFlow(
        AddPetSetupScreenContractState(isLoading = false)
    )

    fun initialize(petId: String, onContinue: () -> Unit) {
        viewModelScope.launch {
            if (viewState.value.isComplete) {
                onContinue()
            } else {
                getPetUseCase.getPet(petId).collect { pet ->
                    viewState.update { it.copy(pet = pet) }
                }
            }
        }
    }

    fun markComplete() {
        viewState.update { it.copy(isComplete = true, pet = null) }
    }
}

data class AddPetSetupScreenContractState(
    val pet: Pet? = null,
    val isLoading: Boolean = false,
    val isComplete: Boolean = false
)