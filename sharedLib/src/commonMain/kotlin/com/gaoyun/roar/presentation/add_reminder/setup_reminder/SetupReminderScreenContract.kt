package com.gaoyun.roar.presentation.add_reminder.setup_reminder

import com.gaoyun.roar.model.domain.interactions.InteractionTemplate
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.interactions.InteractionGroup
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfig
import com.gaoyun.roar.model.domain.interactions.InteractionType
import com.gaoyun.roar.presentation.ViewEvent
import com.gaoyun.roar.presentation.ViewSideEffect
import com.gaoyun.roar.presentation.ViewState
import kotlinx.datetime.LocalDateTime

class SetupReminderScreenContract {

    sealed class Event : ViewEvent {
        class RepeatConfigChanged(val config: String) : Event()
        class OnSaveButtonClick(
            val name: String,
            val type: InteractionType,
            val repeatIsEnabled: Boolean,
            val repeatConfig: InteractionRepeatConfig,
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
        val repeatConfig: InteractionRepeatConfig = InteractionRepeatConfig()
    ) : ViewState

    sealed class Effect : ViewSideEffect {
//        class TemplateChosen(val templateId: String) : Effect()

        sealed class Navigation : Effect() {
//            class ToReminderSetup(val templateId: String) : Navigation()
            object NavigateBack : Navigation()
        }
    }
}