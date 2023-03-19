package com.gaoyun.roar.presentation.user_edit

import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.presentation.ViewEvent
import com.gaoyun.roar.presentation.ViewSideEffect
import com.gaoyun.roar.presentation.ViewState

class EditUserScreenContract {

    sealed class Event : ViewEvent {
        class OnSaveAccountClick(val user: User) : Event()
    }

    data class State(
        val isLoading: Boolean = false,
        val userToEdit: User? = null,
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect() {
            object NavigateBack : Navigation()
        }
    }

}