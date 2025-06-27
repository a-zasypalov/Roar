package com.gaoyun.roar.presentation.add_reminder.complete

import com.gaoyun.roar.ui.navigation.NavigationSideEffect

class AddReminderCompleteScreenContract {
    sealed class Effect {
        sealed class Navigation : Effect(), NavigationSideEffect {
            object Continue : Navigation()
        }
    }
}