package com.gaoyun.roar.presentation.interactions

import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.domain.interaction.InsertInteraction
import com.gaoyun.roar.domain.interaction.RemoveInteraction
import com.gaoyun.roar.domain.interaction.ActivateInteraction
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.domain.reminder.RemoveReminder
import com.gaoyun.roar.domain.reminder.SetReminderComplete
import com.gaoyun.roar.model.domain.interactions.withoutReminders
import com.gaoyun.roar.presentation.BaseViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

class InteractionScreenViewModel(
    private val getPetUseCase: GetPetUseCase,
    private val getInteraction: GetInteraction,
    private val saveInteraction: InsertInteraction,
    private val setReminderComplete: SetReminderComplete,
    private val removeReminder: RemoveReminder,
    private val removeInteraction: RemoveInteraction,
    private val activateInteraction: ActivateInteraction,
) : BaseViewModel<InteractionScreenContract.Event, InteractionScreenContract.State, InteractionScreenContract.Effect>() {

    override fun setInitialState() = InteractionScreenContract.State(isLoading = true)

    override fun handleEvents(event: InteractionScreenContract.Event) {
        when (event) {
            is InteractionScreenContract.Event.OnEditClick -> setEffect {
                InteractionScreenContract.Effect.Navigation.ToEditInteraction(
                    petId = event.petId,
                    interaction = event.interaction
                )
            }

            is InteractionScreenContract.Event.OnSaveNotes -> saveNoteState(event.notes)
            is InteractionScreenContract.Event.OnReminderCompleteClick -> setReminderComplete(event.reminderId, event.isComplete, event.completionDateTime)
            is InteractionScreenContract.Event.OnReminderRemoveFromHistoryClick -> {
                if (event.confirmed) {
                    removeReminderFromHistory(event.reminderId)
                } else {
                    setEffect { InteractionScreenContract.Effect.ShowRemoveReminderFromHistoryDialog(event.reminderId) }
                }
            }

            is InteractionScreenContract.Event.OnCompleteReminderNotTodayClick -> {
                setEffect { InteractionScreenContract.Effect.ShowCompleteReminderDialog(event.reminderId, event.date) }
            }

            is InteractionScreenContract.Event.OnActivateButtonClick -> setInteractionIsActive(event.interactionId, event.activate)
            is InteractionScreenContract.Event.OnDeleteButtonClick -> {
                if (event.confirmed) {
                    removeInteraction(event.interactionId)
                } else {
                    setEffect { InteractionScreenContract.Effect.ShowRemoveInteractionDialog }
                }
            }
        }
    }

    fun buildScreenState(interactionId: String) = scope.launch {
        getInteraction.getInteractionWithReminders(interactionId).collect { interaction ->
            getPetUseCase.getPet(interaction.petId).collect { pet ->
                setState { copy(pet = pet, isLoading = false, interaction = interaction) }
            }
        }
    }

    private fun saveNoteState(notes: String) = scope.launch {
        viewState.value.interaction?.let {
            saveInteraction.insertInteraction(it.copy(notes = notes.trim()).withoutReminders()).firstOrNull()
        }
    }

    private fun setReminderComplete(reminderId: String, isComplete: Boolean, completionDateTime: LocalDateTime) = scope.launch {
        setReminderComplete.setComplete(reminderId, isComplete, completionDateTime).collect {
            it?.let { interaction -> setState { copy(interaction = interaction) } }
        }
    }

    private fun removeReminderFromHistory(reminderId: String) = scope.launch {
        removeReminder.removeReminder(reminderId).firstOrNull()
        getInteraction.getInteractionWithReminders(viewState.value.interaction?.id ?: "").collect { interaction ->
            setState { copy(interaction = interaction) }
        }
    }

    private fun setInteractionIsActive(interactionId: String, isActive: Boolean) = scope.launch {
        activateInteraction.setInteractionIsActive(interactionId, isActive).collect { interaction ->
            setState { copy(interaction = interaction) }
        }
    }

    private fun removeInteraction(interactionId: String) = scope.launch {
        removeInteraction.removeInteraction(interactionId).collect {
            setState { copy(interaction = null) }
            setEffect { InteractionScreenContract.Effect.NavigateBack }
        }
    }
}