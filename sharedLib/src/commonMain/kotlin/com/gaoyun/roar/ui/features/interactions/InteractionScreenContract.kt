package com.gaoyun.roar.ui.features.interactions

import kotlinx.datetime.LocalDateTime

class InteractionScreenContract {

    sealed class Event {
        class OnReminderCompleteClick(val reminderId: String, val isComplete: Boolean, val completionDateTime: LocalDateTime) : Event()
        class OnReminderRemoveFromHistoryClick(val reminderId: String, val confirmed: Boolean = false) : Event()
        class OnCompleteReminderNotTodayClick(val reminderId: String, val date: LocalDateTime) : Event()
    }
}