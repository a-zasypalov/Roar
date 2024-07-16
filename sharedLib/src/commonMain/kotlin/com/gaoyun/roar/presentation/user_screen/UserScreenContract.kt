package com.gaoyun.roar.presentation.user_screen

import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.ViewEvent
import com.gaoyun.roar.presentation.ViewSideEffect
import com.gaoyun.roar.presentation.ViewState
import com.gaoyun.roar.util.AppIcon
import com.gaoyun.roar.util.ColorTheme

class UserScreenContract {
    sealed class Event : ViewEvent {
        data object OnDeleteAccountClick : Event()
        data object OnEditAccountClick : Event()
        data object OnLogout : Event()
        data object OnAboutScreenClick : Event()
        data object OnCreateBackupClick : Event()
        data object OnUseBackupClick : Event()
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
        data class OnHomeScreenModeChange(val full: Boolean) : Event()
        data class OnStaticColorThemePick(val theme: ColorTheme) : Event()
        data class OnNumberOfRemindersOnMainScreen(val newNumber: Int) : Event()
        data class OnAppIconChange(val icon: AppIcon) : Event()
        data object NavigateBack : Event()
    }

    data class State(
        val isLoading: Boolean = false,
        val dynamicColorActive: Boolean = false,
        val screenModeFull: Boolean = true,
        val activeColorTheme: ColorTheme = ColorTheme.Orange,
        val numberOfRemindersOnMainScreenState: String = "2",
        val numberOfPets: Int = 1,
        val user: User? = null,
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        data object BackupCreated : Effect()
        data object BackupApplied : Effect()
        data object LoggedOut : Effect()
        data object NavigateBack : Effect()

        sealed class Navigation : Effect(), NavigationSideEffect {
            object ToUserEdit : Navigation()
            object ToAboutScreen : Navigation()
        }
    }
}