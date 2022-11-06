package com.gaoyun.roar.presentation.add_reminder

import com.gaoyun.roar.model.domain.interactions.InteractionTemplate
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.presentation.ViewEvent
import com.gaoyun.roar.presentation.ViewSideEffect
import com.gaoyun.roar.presentation.ViewState

class AddReminderScreenContract {

    sealed class Event : ViewEvent {
        class AddReminderButtonClicked() : Event()
    }

    data class State(
        val isLoading: Boolean = false,
        val pet: Pet? = null,
        val templates: List<InteractionTemplate> = listOf()
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        object ReminderAdded : Effect()

        sealed class Navigation : Effect() {
            object NavigateBack : Navigation()
        }
    }
}