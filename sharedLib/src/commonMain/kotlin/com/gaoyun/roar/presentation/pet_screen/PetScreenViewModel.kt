package com.gaoyun.roar.presentation.pet_screen

import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.domain.interaction.InteractionsListBuilder
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.domain.pet.RemovePetUseCase
import com.gaoyun.roar.domain.reminder.SetReminderComplete
import com.gaoyun.roar.presentation.MultiplatformBaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

class PetScreenViewModel(
    private val getPetUseCase: GetPetUseCase,
    private val getInteraction: GetInteraction,
    private val removePet: RemovePetUseCase,
    private val setReminderComplete: SetReminderComplete,
    private val interactionsListBuilder: InteractionsListBuilder,
) : MultiplatformBaseViewModel<PetScreenContract.Event, PetScreenContract.State, PetScreenContract.Effect>() {

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

            is PetScreenContract.Event.OnEditPetClick -> {
                viewState.value.pet?.let { pet -> setEffect { PetScreenContract.Effect.Navigation.ToEditPet(pet) } }
            }

            is PetScreenContract.Event.OnDeletePetConfirmed -> {
                scope.launch {
                    hideDeletePetDialog()
                    delay(250)
                    removePet.removePet(event.petId)
                        .map { PetScreenContract.Effect.NavigateBack }
                        .collect { setEffect { it } }
                }
            }

            is PetScreenContract.Event.OnInteractionCheckClicked -> setReminderComplete(event.reminderId, event.completed, event.completionDateTime)
        }
    }

    fun buildScreenState(petId: String) = scope.launch {
        getPetUseCase.getPet(petId).collect { pet ->
            getInteraction.getInteractionByPet(pet.id).collect { interactions ->
                setState {
                    copy(
                        pet = pet,
                        isLoading = false,
                        interactions = interactionsListBuilder.buildInitialInteractionsListForPet(interactions),
                        inactiveInteractions = interactionsListBuilder.buildInactiveInteractionsList(interactions)
                    )
                }
            }
        }
    }

    fun hideDeletePetDialog() {
        setState { copy(deletePetDialogShow = false) }
    }

    private fun setReminderComplete(reminderId: String, isComplete: Boolean, completionDateTime: LocalDateTime) = scope.launch {
        setReminderComplete.setComplete(reminderId, isComplete, completionDateTime).filterNotNull().collect { interaction ->
            setState { copy(interactions = interactionsListBuilder.buildListOnCompletingReminder(viewState.value.interactions, interaction, isComplete)) }
        }
    }
}