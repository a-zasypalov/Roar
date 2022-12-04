package com.gaoyun.roar.presentation.pet_screen

import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.domain.pet.RemovePetUseCase
import com.gaoyun.roar.presentation.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PetScreenViewModel :
    BaseViewModel<PetScreenContract.Event, PetScreenContract.State, PetScreenContract.Effect>(),
    KoinComponent {

    private val getPetUseCase: GetPetUseCase by inject()
    private val getInteraction: GetInteraction by inject()
    private val removePet: RemovePetUseCase by inject()

    override fun setInitialState() = PetScreenContract.State(isLoading = true)

    override fun handleEvents(event: PetScreenContract.Event) {
        when (event) {
            is PetScreenContract.Event.InteractionClicked -> setEffect {
                PetScreenContract.Effect.Navigation.ToInteractionDetails(event.interactionId)
            }
            is PetScreenContract.Event.AddReminderButtonClicked -> setEffect {
                PetScreenContract.Effect.Navigation.ToInteractionTemplates(event.petId)
            }
            is PetScreenContract.Event.OnDeletePetClicked -> {
                setState { copy(deletePetDialogShow = true) }
            }
            is PetScreenContract.Event.OnDeletePetConfirmed -> {
                scope.launch {
                    hideDeletePetDialog()
                    delay(250)
                    removePet.removePet(event.petId)
                        .map { PetScreenContract.Effect.Navigation.NavigateBack }
                        .collect { setEffect { it } }
                }
            }
        }
    }

    fun buildScreenState(petId: String) = scope.launch {
        getPetUseCase.getPet(petId).collect { pet ->
            getInteraction.getInteractionByPet(pet.id).collect { interactions ->
                setState { copy(pet = pet, isLoading = false, interactions = interactions) }
            }
        }
    }

    fun hideDeletePetDialog() {
        setState { copy(deletePetDialogShow = false) }
    }
}