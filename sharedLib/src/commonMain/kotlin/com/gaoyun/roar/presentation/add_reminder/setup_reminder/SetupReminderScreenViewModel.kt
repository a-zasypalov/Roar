package com.gaoyun.roar.presentation.add_reminder.setup_reminder

import com.gaoyun.roar.domain.interaction.InsertInteraction
import com.gaoyun.roar.domain.interaction_template.GetInteractionTemplate
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.domain.reminder.InsertReminder
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.interactions.InteractionGroup
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfig
import com.gaoyun.roar.model.domain.interactions.InteractionType
import com.gaoyun.roar.model.domain.interactions.toInteractionRepeatConfig
import com.gaoyun.roar.presentation.BaseViewModel
import com.gaoyun.roar.util.toLocalDate
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SetupReminderScreenViewModel :
    BaseViewModel<SetupReminderScreenContract.Event, SetupReminderScreenContract.State, SetupReminderScreenContract.Effect>(),
    KoinComponent {

    private val getInteractionTemplateUseCase: GetInteractionTemplate by inject()
    private val getPetUseCase: GetPetUseCase by inject()
    private val insertInteraction: InsertInteraction by inject()
    private val insertReminder: InsertReminder by inject()

    override fun setInitialState() = SetupReminderScreenContract.State(isLoading = true)

    override fun handleEvents(event: SetupReminderScreenContract.Event) {
        when (event) {
            is SetupReminderScreenContract.Event.RepeatConfigChanged -> with(event) {
                setState { copy(repeatConfig = config.toInteractionRepeatConfig()) }
            }
            is SetupReminderScreenContract.Event.OnSaveButtonClick -> with(event) {
                createInteraction(
                    templateId = templateId,
                    petId = petId,
                    group = group,
                    name = name,
                    type = type,
                    repeatConfig = if (repeatIsEnabled) repeatConfig else null,
                    notes = notes,
                    dateTime = Instant.fromEpochMilliseconds(date).toLocalDate().atTime(hour = timeHours, minute = timeMinutes)
                )
            }
        }
    }

    fun buildScreenState(petId: String, templateId: String) = scope.launch {
        getPetUseCase.getPet(petId).collect { pet ->
            if (templateId == "null") {
                setState { copy(isLoading = false, pet = pet) }
            } else {
                getInteractionTemplate(pet, templateId)
            }
        }
    }

    private suspend fun getInteractionTemplate(pet: Pet, templateId: String) {
        getInteractionTemplateUseCase.getInteractionTemplate(templateId, pet.petType)
            .collect { template ->
                setState { copy(isLoading = false, pet = pet, template = template) }
            }
    }

    private fun createInteraction(
        templateId: String?,
        petId: String,
        group: InteractionGroup,
        name: String,
        type: InteractionType,
        repeatConfig: InteractionRepeatConfig?,
        notes: String,
        dateTime: LocalDateTime,
    ) = scope.launch {
        insertInteraction.insertInteraction(
            templateId = templateId,
            petId = petId,
            type = type.toString(),
            name = name,
            group = group.toString(),
            repeatConfig = repeatConfig,
            notes = notes
        ).collect { interaction ->
            insertReminder.insertReminder(interaction.id, dateTime).collect { reminder ->
                setEffect { SetupReminderScreenContract.Effect.ReminderCreated(reminder, interaction) }
            }
        }
    }
}