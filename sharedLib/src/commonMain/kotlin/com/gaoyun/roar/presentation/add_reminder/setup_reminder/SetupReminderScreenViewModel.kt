package com.gaoyun.roar.presentation.add_reminder.setup_reminder

import androidx.lifecycle.viewModelScope
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
import com.gaoyun.roar.model.domain.interactions.InteractionTemplate
import com.gaoyun.roar.model.domain.interactions.InteractionType
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.model.domain.interactions.toInteractionRemindConfig
import com.gaoyun.roar.model.domain.interactions.toInteractionRepeatConfig
import com.gaoyun.roar.model.domain.interactions.withoutReminders
import com.gaoyun.roar.presentation.BaseViewModel
import com.gaoyun.roar.util.randomUUID
import com.gaoyun.roar.util.toLocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class SetupReminderScreenViewModel(
    private val getInteractionTemplateUseCase: GetInteractionTemplate,
    private val getPetUseCase: GetPetUseCase,
    private val insertInteraction: InsertInteraction,
    private val insertReminder: InsertReminder,
    private val getInteraction: GetInteraction,
    private val notificationScheduler: NotificationScheduler,
) : BaseViewModel() {

    override val viewState = MutableStateFlow(
        SetupReminderScreenContractState(isLoading = true)
    )

    fun initialize(petId: String, templateId: String, interactionId: String?) = viewModelScope.launch {
        getPetUseCase.getPet(petId).filterNotNull().collect { pet ->
            val interaction = interactionId?.let {
                getInteraction.getInteractionWithReminders(it).firstOrNull()
            }

            if (templateId == "null") {
                viewState.update {
                    it.copy(
                        isLoading = false,
                        pet = pet,
                        interactionToEdit = interaction,
                        repeatConfig = interaction?.repeatConfig ?: InteractionRepeatConfig(),
                        remindConfig = interaction?.remindConfig ?: InteractionRemindConfig()
                    )
                }
            } else {
                getInteractionTemplateUseCase
                    .getInteractionTemplate(templateId, pet.petType)
                    .collect { template ->
                        viewState.update {
                            it.copy(
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
        }
    }

    fun updateRepeatConfig(config: String) {
        viewState.update { it.copy(repeatConfig = config.toInteractionRepeatConfig()) }
    }

    fun updateRemindConfig(config: String) {
        viewState.update { it.copy(remindConfig = config.toInteractionRemindConfig()) }
    }

    fun createOrUpdateInteraction(
        templateId: String?,
        petId: String,
        group: InteractionGroup,
        name: String,
        type: InteractionType,
        repeatIsEnabled: Boolean,
        repeatConfig: InteractionRepeatConfig,
        remindConfig: InteractionRemindConfig,
        notes: String,
        date: Long,
        timeHours: Int,
        timeMinutes: Int,
        onBackToTemplates: () -> Unit,
        onToComplete: (petAvatar: String, petId: String, templateId: String) -> Unit
    ) = viewModelScope.launch {
        val dateTime = Instant.fromEpochMilliseconds(date)
            .toLocalDate()
            .atTime(hour = timeHours, minute = timeMinutes)

        val interactionToEdit = viewState.value.interactionToEdit

        if (interactionToEdit != null) {
            insertInteraction.insertInteraction(
                interactionToEdit.copy(
                    name = name,
                    notes = notes,
                    type = type,
                    group = group,
                    repeatConfig = if (repeatIsEnabled) repeatConfig else null,
                    remindConfig = remindConfig
                ).withoutReminders()
            ).firstOrNull() ?: return@launch

            val reminderToInsert = interactionToEdit.reminders
                .filter { !it.isCompleted }
                .maxBy { it.dateTime }
                .copy(dateTime = dateTime)

            val notificationDateTime = dateTime
                .toInstant(TimeZone.currentSystemDefault())
                .minus(remindConfig.toDuration())
                .toLocalDateTime(TimeZone.currentSystemDefault())

            val notificationData = NotificationData(
                scheduled = notificationDateTime,
                item = NotificationItem.Reminder(
                    workId = reminderToInsert.notificationJobId ?: randomUUID(),
                    itemId = reminderToInsert.id
                )
            )

            notificationScheduler.scheduleNotification(notificationData)

            insertReminder.insertReminder(reminderToInsert).collect {
                onBackToTemplates()
            }
        } else {
            val interaction = insertInteraction.insertInteraction(
                templateId = templateId,
                petId = petId,
                type = type.toString(),
                name = name,
                group = group.toString(),
                repeatConfig = if (repeatIsEnabled) repeatConfig else null,
                remindConfig = remindConfig,
                notes = notes
            ).firstOrNull() ?: return@launch

            insertReminder.createReminder(interaction.id, dateTime, remindConfig).collect {
                if (type == InteractionType.CUSTOM) {
                    onToComplete(
                        viewState.value.pet?.avatar.orEmpty(),
                        viewState.value.pet?.id.orEmpty(),
                        viewState.value.template?.id.orEmpty()
                    )
                } else {
                    onBackToTemplates()
                }
            }
        }
    }
}

data class SetupReminderScreenContractState(
    val isLoading: Boolean = false,
    val pet: Pet? = null,
    val template: InteractionTemplate? = null,
    val interactionToEdit: InteractionWithReminders? = null,
    val repeatConfig: InteractionRepeatConfig = InteractionRepeatConfig(),
    val remindConfig: InteractionRemindConfig = InteractionRemindConfig()
)