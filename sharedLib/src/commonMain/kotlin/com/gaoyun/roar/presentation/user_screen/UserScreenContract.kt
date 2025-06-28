package com.gaoyun.roar.presentation.user_screen

import com.gaoyun.roar.ui.navigation.NavigationSideEffect
import com.gaoyun.roar.util.AppIcon
import com.gaoyun.roar.util.ColorTheme

class UserScreenContract {
    sealed class Event {
        data object OnLogout : Event()
        data object OnAccountDeleteConfirmed : Event()
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
    }

    sealed class Effect {
        sealed class Navigation : Effect(), NavigationSideEffect {
            object ToUserEdit : Navigation()
            object ToAboutScreen : Navigation()
        }
    }
}