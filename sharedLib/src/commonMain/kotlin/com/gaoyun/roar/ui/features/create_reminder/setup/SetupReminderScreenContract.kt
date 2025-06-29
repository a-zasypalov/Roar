package com.gaoyun.roar.presentation.add_reminder.setup_reminder

import com.gaoyun.roar.model.domain.Reminder
import com.gaoyun.roar.ui.navigation.NavigationSideEffect

class SetupReminderScreenContract {
    sealed class Effect {
        sealed class Navigation : Effect(), NavigationSideEffect {
            object BackToTemplates : Navigation()
            class ToComplete(val petAvatar: String, val petId: String, val templateId: String) : Navigation()
        }
    }
}