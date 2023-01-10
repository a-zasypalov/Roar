package com.gaoyun.roar.presentation.interactions

import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.presentation.ViewEvent
import com.gaoyun.roar.presentation.ViewSideEffect
import com.gaoyun.roar.presentation.ViewState
import kotlinx.datetime.LocalDateTime

class InteractionScreenContract {

    sealed class Event : ViewEvent {
        class OnActivateButtonClick(val interactionId: String, val activate: Boolean) : Event()
        class OnDeleteButtonClick(val interactionId: String, val confirmed: Boolean = false) : Event()
        class OnSaveNotes(val notes: String) : Event()
        class OnReminderCompleteClick(val reminderId: String, val isComplete: Boolean, val completionDateTime: LocalDateTime) : Event()
        class OnReminderRemoveFromHistoryClick(val reminderId: String, val confirmed: Boolean = false) : Event()
        class OnCompleteReminderNotTodayClick(val reminderId: String, val date: LocalDateTime) : Event()
    }

    data class State(
        val isLoading: Boolean = false,
        val pet: Pet? = null,
        val interaction: InteractionWithReminders? = null
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        class ShowRemoveReminderFromHistoryDialog(val reminderId: String) : Effect()
        class ShowCompleteReminderDialog(val reminderId: String, val date: LocalDateTime) : Effect()
        object ShowRemoveInteractionDialog : Effect()
        sealed class Navigation : Effect() {
            object NavigateBack : Navigation()
            class ToEditInteraction(val petId: String, val interaction: InteractionWithReminders) : Navigation()
        }
    }
}