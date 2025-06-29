package com.gaoyun.roar.ui.features.pet

import androidx.lifecycle.viewModelScope
import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.domain.interaction.InteractionsListBuilder
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.domain.pet.RemovePetUseCase
import com.gaoyun.roar.domain.reminder.SetReminderComplete
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.interactions.InteractionGroup
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.ui.common.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

data class PetScreenState(
    val isLoading: Boolean = false,
    val pet: Pet? = null,
    val interactions: Map<InteractionGroup, List<InteractionWithReminders>> = mapOf(),
    val inactiveInteractions: List<InteractionWithReminders> = listOf(),
    val deletePetDialogShow: Boolean = false,
)

class PetScreenViewModel(
    private val getPetUseCase: GetPetUseCase,
    private val getInteraction: GetInteraction,
    private val removePet: RemovePetUseCase,
    private val setReminderComplete: SetReminderComplete,
    private val interactionsListBuilder: InteractionsListBuilder,
) : BaseViewModel() {
    override val viewState = MutableStateFlow(PetScreenState(isLoading = true))

    init {
        // optionally, could load default
    }

    fun loadPet(petId: String) = viewModelScope.launch {
        viewState.update { it.copy(isLoading = true) }
        getPetUseCase.getPet(petId).filterNotNull().collect { pet ->
            getInteraction.getInteractionByPet(pet.id).collect { interactions ->
                viewState.update {
                    it.copy(
                        pet = pet,
                        isLoading = false,
                        interactions = interactionsListBuilder.buildInitialInteractionsListForPet(interactions),
                        inactiveInteractions = interactionsListBuilder.buildInactiveInteractionsList(interactions)
                    )
                }
            }
        }
    }

    fun confirmDelete() = viewModelScope.launch {
        viewState.update { it.copy(deletePetDialogShow = false) }
        delay(250)
        viewState.value.pet?.id?.let { removePet.removePet(it) }
    }

    fun showDeleteConfirmDialog() {
        viewState.update { it.copy(deletePetDialogShow = true) }
    }

    fun hideDeleteConfirmDialog() {
        viewState.update { it.copy(deletePetDialogShow = false) }
    }

    fun completeReminder(reminderId: String, isComplete: Boolean, dateTime: LocalDateTime) = viewModelScope.launch {
        setReminderComplete.setComplete(reminderId, isComplete, dateTime).filterNotNull().collect { interaction ->
            viewState.update {
                it.copy(
                    interactions = interactionsListBuilder.buildListOnCompletingReminder(
                        it.interactions, interaction, isComplete
                    )
                )
            }
        }
    }
}