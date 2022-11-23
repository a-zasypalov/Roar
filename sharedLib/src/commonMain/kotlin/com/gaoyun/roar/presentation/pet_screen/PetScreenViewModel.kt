package com.gaoyun.roar.presentation.pet_screen

import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.presentation.BaseViewModel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PetScreenViewModel :
    BaseViewModel<PetScreenContract.Event, PetScreenContract.State, PetScreenContract.Effect>(),
    KoinComponent {

    private val getPetUseCase: GetPetUseCase by inject()
    private val getInteraction: GetInteraction by inject()

    override fun setInitialState() = PetScreenContract.State(isLoading = true)

    override fun handleEvents(event: PetScreenContract.Event) {
        when (event) {
            is PetScreenContract.Event.InteractionClicked -> setEffect {
                PetScreenContract.Effect.Navigation.ToInteractionDetails(event.interactionId)
            }
            is PetScreenContract.Event.AddReminderButtonClicked -> setEffect {
                PetScreenContract.Effect.Navigation.ToInteractionTemplates(event.petId)
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
}