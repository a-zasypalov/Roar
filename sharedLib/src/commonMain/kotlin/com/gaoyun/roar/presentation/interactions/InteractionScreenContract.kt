package com.gaoyun.roar.presentation.interactions

import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.presentation.ViewEvent
import com.gaoyun.roar.presentation.ViewSideEffect
import com.gaoyun.roar.presentation.ViewState

class InteractionScreenContract {

    sealed class Event : ViewEvent {
        class OnEditButtonClick(val interactionId: String?): Event()
        class OnSaveNotes(val notes: String): Event()
    }

    data class State(
        val isLoading: Boolean = false,
        val pet: Pet? = null,
        val interaction: InteractionWithReminders? = null
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect() {
            object NavigateBack : Navigation()
        }
    }
}