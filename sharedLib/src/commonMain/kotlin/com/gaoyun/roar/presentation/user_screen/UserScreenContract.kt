package com.gaoyun.roar.presentation.user_screen

import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.presentation.ViewEvent
import com.gaoyun.roar.presentation.ViewSideEffect
import com.gaoyun.roar.presentation.ViewState

class UserScreenContract {

    sealed class Event : ViewEvent {
        object OnDeleteAccountClick : Event()
        object OnEditAccountClick : Event()
        object OnCreateBackupClick: Event()
        object OnUseBackupClick: Event()
    }

    data class State(
        val isLoading: Boolean = false,
        val user: User? = null,
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        object BackupReady: Effect()

        sealed class Navigation : Effect() {
            object NavigateBack : Navigation()
        }
    }

}