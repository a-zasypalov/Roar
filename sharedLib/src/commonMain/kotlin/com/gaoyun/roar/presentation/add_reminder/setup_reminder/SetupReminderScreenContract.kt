package com.gaoyun.roar.presentation.add_reminder.setup_reminder

import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.Reminder
import com.gaoyun.roar.model.domain.interactions.*
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.ViewEvent
import com.gaoyun.roar.presentation.ViewSideEffect
import com.gaoyun.roar.presentation.ViewState

class SetupReminderScreenContract {

    sealed class Event : ViewEvent {

        class RepeatConfigChanged(val config: String) : Event()
        class RemindConfigChanged(val config: String) : Event()
        class OnSaveButtonClick(
            val name: String,
            val type: InteractionType,
            val repeatIsEnabled: Boolean,
            val repeatConfig: InteractionRepeatConfig,
            val remindConfig: InteractionRemindConfig,
            val templateId: String?,
            val petId: String,
            val group: InteractionGroup,
            val notes: String,
            val date: Long,
            val timeHours: Int,
            val timeMinutes: Int
        ) : Event()
    }

    data class State(
        val isLoading: Boolean = false,
        val pet: Pet? = null,
        val template: InteractionTemplate? = null,
        val interactionToEdit: InteractionWithReminders? = null,
        val repeatConfig: InteractionRepeatConfig = InteractionRepeatConfig(),
        val remindConfig: InteractionRemindConfig = InteractionRemindConfig()
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        class ReminderSaved(val reminder: Reminder) : Effect()
        sealed class Navigation : Effect(), NavigationSideEffect {
            object BackToTemplates : Navigation()
            class ToComplete(val petAvatar: String, val petId: String, val templateId: String) : Navigation()
        }
    }
}