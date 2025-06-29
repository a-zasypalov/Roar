package com.gaoyun.roar.ui.features.create_reminder.choose_template

import com.gaoyun.roar.ui.navigation.NavigationSideEffect

class AddReminderScreenContract {

    sealed class Event {
        class TemplateChosen(val petId: String, val templateId: String) : Event()
    }

    sealed class Effect {
        sealed class Navigation : Effect(), NavigationSideEffect {
            class ToReminderSetup(val petId: String, val templateId: String) : Navigation()
        }
    }
}