package com.gaoyun.roar.presentation.user_screen

import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.presentation.ViewEvent
import com.gaoyun.roar.presentation.ViewSideEffect
import com.gaoyun.roar.presentation.ViewState

class UserScreenContract {
    sealed class Event : ViewEvent {
        object OnDeleteAccountClick : Event()
        object OnEditAccountClick : Event()
        object OnLogout : Event()
        object OnCreateBackupClick : Event()
        data class OnUseBackup(val backup: ByteArray, val removeOld: Boolean) : Event() {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other == null || this::class != other::class) return false

                other as OnUseBackup

                if (!backup.contentEquals(other.backup)) return false
                if (removeOld != other.removeOld) return false

                return true
            }

            override fun hashCode(): Int {
                var result = backup.contentHashCode()
                result = 31 * result + removeOld.hashCode()
                return result
            }
        }

        data class OnDynamicColorsStateChange(val active: Boolean) : Event()
        data class OnNumberOfRemindersOnMainScreen(val newNumber: Int) : Event()
        object NavigateBack : Event()
    }

    data class State(
        val isLoading: Boolean = false,
        val dynamicColorActive: Boolean = false,
        val numberOfRemindersOnMainScreenState: String = "2",
        val user: User? = null,
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        object BackupReady : Effect()
        object BackupApplied : Effect()
        object LoggedOut : Effect()

        sealed class Navigation : Effect() {
            object NavigateBack : Navigation()
            object ToUserEdit : Navigation()
        }
    }

}