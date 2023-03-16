package com.gaoyun.roar.presentation.add_reminder.choose_template

import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.PetWithInteractions
import com.gaoyun.roar.model.domain.interactions.InteractionTemplate
import com.gaoyun.roar.presentation.ViewEvent
import com.gaoyun.roar.presentation.ViewSideEffect
import com.gaoyun.roar.presentation.ViewState

class AddReminderScreenContract {

    sealed class Event : ViewEvent {
        class TemplateChosen(val petId: String, val templateId: String) : Event()
    }

    data class State(
        val isLoading: Boolean = false,
        val pet: PetWithInteractions? = null,
        val templates: List<InteractionTemplate> = listOf()
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect() {
            class ToReminderSetup(val petId: String, val templateId: String) : Navigation()
            object NavigateBack : Navigation()
        }
    }
}