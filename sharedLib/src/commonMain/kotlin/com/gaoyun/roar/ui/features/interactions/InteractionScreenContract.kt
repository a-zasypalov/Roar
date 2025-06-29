package com.gaoyun.roar.ui.features.interactions

import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.ui.navigation.NavigationSideEffect
import kotlinx.datetime.LocalDateTime

class InteractionScreenContract {

    sealed class Event {
        class OnReminderCompleteClick(val reminderId: String, val isComplete: Boolean, val completionDateTime: LocalDateTime) : Event()
        class OnReminderRemoveFromHistoryClick(val reminderId: String, val confirmed: Boolean = false) : Event()
        class OnCompleteReminderNotTodayClick(val reminderId: String, val date: LocalDateTime) : Event()
    }

    sealed class Effect {
        data object NavigateBack : Effect()
        sealed class Navigation : Effect(), NavigationSideEffect {
            class ToEditInteraction(val petId: String, val interaction: InteractionWithReminders) : Navigation()
        }
    }
}