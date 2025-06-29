package com.gaoyun.roar.ui.features.interactions

import androidx.lifecycle.viewModelScope
import com.gaoyun.roar.domain.interaction.ActivateInteraction
import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.domain.interaction.InsertInteraction
import com.gaoyun.roar.domain.interaction.RemoveInteraction
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.domain.reminder.RemoveReminder
import com.gaoyun.roar.domain.reminder.SetReminderComplete
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.model.domain.interactions.withoutReminders
import com.gaoyun.roar.ui.common.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
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
) : BaseViewModel() {

    override val viewState = MutableStateFlow(
        InteractionScreenState(isLoading = true)
    )

    fun buildScreenState(interactionId: String) = viewModelScope.launch {
        getInteraction.getInteractionWithReminders(interactionId).collect { interaction ->
            getPetUseCase.getPet(interaction.petId).collect { pet ->
                viewState.update { it.copy(pet = pet, isLoading = false, interaction = interaction) }
            }
        }
    }

    fun onSaveNotes(notes: String) = viewModelScope.launch {
        viewState.value.interaction?.let {
            saveInteraction.insertInteraction(it.copy(notes = notes.trim()).withoutReminders()).firstOrNull()
        }
    }

    fun onReminderComplete(reminderId: String, isComplete: Boolean, completionDateTime: LocalDateTime) = viewModelScope.launch {
        setReminderComplete.setComplete(reminderId, isComplete, completionDateTime)
            .filterNotNull()
            .collect { updatedInteraction ->
                viewState.update { it.copy(interaction = updatedInteraction) }
            }
    }

    fun removeReminderFromHistory(reminderId: String) = viewModelScope.launch {
        removeReminder.removeReminder(reminderId).firstOrNull()
        getInteraction.getInteractionWithReminders(viewState.value.interaction?.id ?: "").collect { interaction ->
            viewState.update { it.copy(interaction = interaction) }
        }
    }

    fun onActivateInteraction(interactionId: String, isActive: Boolean) = viewModelScope.launch {
        activateInteraction.setInteractionIsActive(interactionId, isActive).collect { updatedInteraction ->
            viewState.update { it.copy(interaction = updatedInteraction) }
        }
    }

    fun onDeleteInteraction(interactionId: String, onDeleted: () -> Unit) = viewModelScope.launch {
        removeInteraction.removeInteraction(interactionId).firstOrNull()
        viewState.update { it.copy(interaction = null) }
        onDeleted()
    }
}

data class InteractionScreenState(
    val isLoading: Boolean = false,
    val pet: Pet? = null,
    val interaction: InteractionWithReminders? = null
)