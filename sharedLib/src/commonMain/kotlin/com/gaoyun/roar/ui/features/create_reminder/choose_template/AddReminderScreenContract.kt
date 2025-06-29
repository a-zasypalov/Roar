package com.gaoyun.roar.ui.features.create_reminder.choose_template

class AddReminderScreenContract {

    sealed class Event {
        class TemplateChosen(val petId: String, val templateId: String) : Event()
    }
}