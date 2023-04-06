package com.gaoyun.roar.presentation.home_screen

import com.gaoyun.roar.model.domain.PetWithInteractions
import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.presentation.ViewEvent
import com.gaoyun.roar.presentation.ViewSideEffect
import com.gaoyun.roar.presentation.ViewState
import kotlinx.datetime.LocalDateTime

class HomeScreenContract {

    sealed class Event : ViewEvent {
        class LoginUser(val id: String) : Event()
        class SetPetChooserShow(val show: Boolean) : Event()
        class PetChosenForReminderCreation(val petId: String) : Event()
        class InteractionClicked(val petId: String, val interactionId: String) : Event()
        class OnDeletePetClicked(val pet: PetWithInteractions) : Event()
        class OnDeletePetConfirmed(val pet: PetWithInteractions) : Event()
        class OnInteractionCheckClicked(val pet: PetWithInteractions, val reminderId: String, val completed: Boolean, val completionDateTime: LocalDateTime) : Event()
    }

    data class State(
        val user: User? = null,
        val pets: List<PetWithInteractions> = emptyList(),
        val isLoading: Boolean = false,
        val showPetChooser: Boolean = false,
        val deletePetDialogShow: Boolean = false,
        val showLastReminder: Boolean = false,
        val remindersPerPet: Int = 2,
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect() {
            object NavigateBack : Navigation()
            object ToUserRegistration : Navigation()
            object ToAddPet : Navigation()
            class ToAddReminder(val petId: String) : Navigation()
            class ToPetScreen(val petId: String) : Navigation()
            class ToInteractionDetails(val interactionId: String) : Navigation()
            class ToEditPet(val pet: PetWithInteractions) : Navigation()
            object ToUserScreen : Navigation()
        }
    }
}