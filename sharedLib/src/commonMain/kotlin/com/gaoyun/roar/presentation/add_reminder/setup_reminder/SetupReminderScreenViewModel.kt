package com.gaoyun.roar.presentation.add_reminder.setup_reminder

import com.gaoyun.roar.domain.NotificationScheduler
import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.domain.interaction.InsertInteraction
import com.gaoyun.roar.domain.interaction_template.GetInteractionTemplate
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.domain.reminder.InsertReminder
import com.gaoyun.roar.model.domain.NotificationData
import com.gaoyun.roar.model.domain.NotificationItem
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.interactions.InteractionGroup
import com.gaoyun.roar.model.domain.interactions.InteractionRemindConfig
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfig
import com.gaoyun.roar.model.domain.interactions.InteractionType
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.model.domain.interactions.toInteractionRemindConfig
import com.gaoyun.roar.model.domain.interactions.toInteractionRepeatConfig
import com.gaoyun.roar.model.domain.interactions.withoutReminders
import com.gaoyun.roar.presentation.BaseViewModel
import com.gaoyun.roar.util.randomUUID
import com.gaoyun.roar.util.toLocalDate
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atTime

class SetupReminderScreenViewModel(
    private val getInteractionTemplateUseCase: GetInteractionTemplate,
    private val getPetUseCase: GetPetUseCase,
    private val insertInteraction: InsertInteraction,
    private val insertReminder: InsertReminder,
    private val getInteraction: GetInteraction,
    private val notificationScheduler: NotificationScheduler,
) : BaseViewModel<SetupReminderScreenContract.Event, SetupReminderScreenContract.State, SetupReminderScreenContract.Effect>() {

    override fun setInitialState() = SetupReminderScreenContract.State(isLoading = true)

    override fun handleEvents(event: SetupReminderScreenContract.Event) {
        when (event) {
            is SetupReminderScreenContract.Event.RepeatConfigChanged -> with(event) {
                setState { copy(repeatConfig = config.toInteractionRepeatConfig()) }
            }

            is SetupReminderScreenContract.Event.RemindConfigChanged -> with(event) {
                setState { copy(remindConfig = config.toInteractionRemindConfig()) }
            }

            is SetupReminderScreenContract.Event.OnSaveButtonClick -> with(event) {
                createOrUpdateInteraction(
                    templateId = templateId,
                    petId = petId,
                    group = group,
                    name = name,
                    type = type,
                    repeatConfig = if (repeatIsEnabled) repeatConfig else null,
                    remindConfig = remindConfig,
                    notes = notes,
                    dateTime = Instant.fromEpochMilliseconds(date).toLocalDate().atTime(hour = timeHours, minute = timeMinutes)
                )
            }
        }
    }

    fun buildScreenState(petId: String, templateId: String, interactionId: String?) = scope.launch {
        getPetUseCase.getPet(petId).collect { pet ->
            val interaction = interactionId?.let { getInteraction.getInteractionWithReminders(it).firstOrNull() }
            if (templateId == "null") {
                setState {
                    copy(
                        isLoading = false,
                        pet = pet,
                        interactionToEdit = interaction,
                        repeatConfig = interaction?.repeatConfig ?: InteractionRepeatConfig(),
                        remindConfig = interaction?.remindConfig ?: InteractionRemindConfig(),
                    )
                }
            } else {
                getInteractionTemplate(pet, templateId, interaction)
            }
        }
    }

    private suspend fun getInteractionTemplate(pet: Pet, templateId: String, interaction: InteractionWithReminders?) {
        getInteractionTemplateUseCase.getInteractionTemplate(templateId, pet.petType)
            .collect { template ->
                setState {
                    copy(
                        isLoading = false,
                        pet = pet,
                        template = template,
                        repeatConfig = interaction?.repeatConfig ?: template?.repeatConfig ?: InteractionRepeatConfig(),
                        remindConfig = interaction?.remindConfig ?: InteractionRemindConfig(),
                        interactionToEdit = interaction
                    )
                }
            }
    }

    private fun createOrUpdateInteraction(
        templateId: String?,
        petId: String,
        group: InteractionGroup,
        name: String,
        type: InteractionType,
        repeatConfig: InteractionRepeatConfig?,
        remindConfig: InteractionRemindConfig,
        notes: String,
        dateTime: LocalDateTime,
    ) = scope.launch {
        val interactionToEdit = viewState.value.interactionToEdit
        if (interactionToEdit != null) {
            insertInteraction.insertInteraction(
                interactionToEdit.copy(
                    name = name,
                    notes = notes,
                    type = type,
                    group = group,
                    repeatConfig = repeatConfig,
                    remindConfig = remindConfig
                ).withoutReminders()
            ).firstOrNull() ?: return@launch
            val reminderToInsert = interactionToEdit.reminders.filter { !it.isCompleted }.maxBy { it.dateTime }.copy(dateTime = dateTime)

            val notificationData = NotificationData(
                scheduled = reminderToInsert.dateTime,
                item = NotificationItem.Reminder(
                    workId = reminderToInsert.notificationJobId ?: randomUUID(),
                    itemId = reminderToInsert.id
                )
            )
            notificationScheduler.scheduleNotification(notificationData)

            insertReminder.insertReminder(reminderToInsert).collect {
                setEffect { SetupReminderScreenContract.Effect.ReminderSaved(it) }
            }
        } else {
            val interaction = insertInteraction.insertInteraction(
                templateId = templateId,
                petId = petId,
                type = type.toString(),
                name = name,
                group = group.toString(),
                repeatConfig = repeatConfig,
                remindConfig = remindConfig,
                notes = notes
            ).firstOrNull() ?: return@launch

            insertReminder.createReminder(interaction.id, dateTime, remindConfig).collect { _ ->
                setEffect {
                    if (type == InteractionType.CUSTOM) {
                        SetupReminderScreenContract.Effect.Navigation.ToComplete(
                            petAvatar = viewState.value.pet?.avatar.toString(),
                            petId = viewState.value.pet?.id.toString(),
                            templateId = viewState.value.template?.id.toString()
                        )
                    } else {
                        SetupReminderScreenContract.Effect.Navigation.BackToTemplates
                    }
                }
            }
        }
    }
}