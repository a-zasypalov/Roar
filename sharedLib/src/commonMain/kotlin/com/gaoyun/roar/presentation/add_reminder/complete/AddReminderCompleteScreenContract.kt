package com.gaoyun.roar.presentation.add_reminder.complete

import com.gaoyun.roar.presentation.ViewEvent
import com.gaoyun.roar.presentation.ViewSideEffect
import com.gaoyun.roar.presentation.ViewState

class AddReminderCompleteScreenContract {
    sealed class Event : ViewEvent {
        object ContinueButtonClicked : Event()
    }

    data class State(
        val isLoading: Boolean = false
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        object Continue : Effect()

        sealed class Navigation : Effect() {
            object Complete : Navigation()
            object NavigateBack : Navigation()
        }
    }
}