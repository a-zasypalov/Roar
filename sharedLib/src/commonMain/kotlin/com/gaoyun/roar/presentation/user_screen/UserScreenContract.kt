package com.gaoyun.roar.presentation.user_screen

import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.presentation.ViewEvent
import com.gaoyun.roar.presentation.ViewSideEffect
import com.gaoyun.roar.presentation.ViewState

class UserScreenContract {

    sealed class Event : ViewEvent {
        object OnDeleteAccountClick : Event()
        object OnEditAccountClick : Event()
        object OnCreateBackupClick : Event()
        data class OnUseBackup(val backupString: String, val removeOld: Boolean) : Event()
        data class OnDynamicColorsStateChange(val active: Boolean) : Event()
    }

    data class State(
        val isLoading: Boolean = false,
        val dynamicColorActive: Boolean = false,
        val user: User? = null,
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        object BackupReady : Effect()
        object BackupApplied : Effect()

        sealed class Navigation : Effect() {
            object NavigateBack : Navigation()
            object ToUserEdit : Navigation()
        }
    }

}