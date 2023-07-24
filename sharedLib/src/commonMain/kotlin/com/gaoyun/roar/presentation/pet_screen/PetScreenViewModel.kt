package com.gaoyun.roar.presentation.pet_screen

import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.domain.pet.RemovePetUseCase
import com.gaoyun.roar.domain.reminder.SetReminderComplete
import com.gaoyun.roar.model.domain.interactions.withReminders
import com.gaoyun.roar.presentation.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

class PetScreenViewModel(
    private val getPetUseCase: GetPetUseCase,
    private val getInteraction: GetInteraction,
    private val removePet: RemovePetUseCase,
    private val setReminderComplete: SetReminderComplete,
) : BaseViewModel<PetScreenContract.Event, PetScreenContract.State, PetScreenContract.Effect>() {

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
                        interactions = interactions
                            .filter { it.reminders.any { r -> !r.isCompleted } }
                            .map {
                                it.withReminders(it.reminders.toMutableList().filter { !it.isCompleted })
                            }
                            .sortedBy { v ->
                                v.reminders.filter { r -> !r.isCompleted }.minOfOrNull { r -> r.dateTime }
                                    ?: LocalDateTime(LocalDate.fromEpochDays(0), LocalTime(0, 0, 0))
                            }
                            .groupBy { it.group },
                        inactiveInteractions = interactions
                            .filter { it.reminders.all { r -> r.isCompleted } }
                            .sortedByDescending { v ->
                                v.reminders.maxOfOrNull { r -> r.dateTime }
                                    ?: LocalDateTime(LocalDate.fromEpochDays(0), LocalTime(0, 0, 0))
                            },
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
            val newInteractions = viewState.value.interactions.toMutableMap()
            val newList = newInteractions[interaction.group]?.toMutableList()?.apply {
                indexOfFirst { item -> item.id == interaction.id }.takeIf { it > -1 }?.let {
                    val interactionToShow = if (isComplete) interaction else interaction.withReminders(interaction.reminders.filter { !it.isCompleted })
                    set(it, interactionToShow)
                }
            }
            newInteractions[interaction.group] = newList ?: emptyList()
            setState { copy(interactions = newInteractions, showLastReminder = showLastReminder || isComplete) }
        }
    }
}