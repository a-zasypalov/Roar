package com.gaoyun.roar.ui.features.create_reminder.setup

import com.gaoyun.roar.ui.navigation.NavigationSideEffect

class SetupReminderScreenContract {
    sealed class Effect {
        sealed class Navigation : Effect(), NavigationSideEffect {
            class BackToTemplates(val petId: String) : Navigation()
            class ToComplete(val petAvatar: String, val petId: String, val templateId: String) : Navigation()
        }
    }
}