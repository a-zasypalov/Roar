package com.gaoyun.roar.presentation.add_reminder.complete

import com.gaoyun.roar.presentation.NavigationSideEffect
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
        sealed class Navigation : Effect(), NavigationSideEffect {
            object Continue : Navigation()
        }
    }
}