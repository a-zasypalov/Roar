package com.gaoyun.roar.presentation.pet_screen

import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.interactions.InteractionGroup
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.ViewEvent
import com.gaoyun.roar.presentation.ViewSideEffect
import com.gaoyun.roar.presentation.ViewState
import kotlinx.datetime.LocalDateTime

class PetScreenContract {

    sealed class Event : ViewEvent {
        class InteractionClicked(val interactionId: String) : Event()
        data object OnDeletePetClicked : Event()
        class AddReminderButtonClicked(val petId: String) : Event()
        class OnDeletePetConfirmed(val petId: String) : Event()
        class OnInteractionCheckClicked(val reminderId: String, val completed: Boolean, val completionDateTime: LocalDateTime) : Event()
        data object OnEditPetClick : Event()
    }

    data class State(
        val isLoading: Boolean = false,
        val pet: Pet? = null,
        val interactions: Map<InteractionGroup, List<InteractionWithReminders>> = mapOf(),
        val inactiveInteractions: List<InteractionWithReminders> = listOf(),
        val deletePetDialogShow: Boolean = false,
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        data object NavigateBack : Effect()
        sealed class Navigation : Effect(), NavigationSideEffect {
            class ToInteractionDetails(val interactionId: String) : Navigation()
            class ToInteractionTemplates(val petId: String) : Navigation()
            class ToEditPet(val pet: Pet) : Navigation()
        }
    }
}