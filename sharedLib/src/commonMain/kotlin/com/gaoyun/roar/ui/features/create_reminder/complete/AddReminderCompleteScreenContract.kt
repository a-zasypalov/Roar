package com.gaoyun.roar.ui.features.create_reminder.complete

import com.gaoyun.roar.ui.navigation.NavigationSideEffect

class AddReminderCompleteScreenContract {
    sealed class Effect {
        sealed class Navigation : Effect(), NavigationSideEffect {
            class Continue(val petId: String) : Navigation()
        }
    }
}