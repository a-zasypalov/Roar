package com.gaoyun.roar.domain.interaction

import com.gaoyun.roar.domain.reminder.AddNextReminder
import com.gaoyun.roar.model.domain.interactions.withReminders
import com.gaoyun.roar.repository.InteractionRepository
import com.gaoyun.roar.repository.ReminderRepository
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ActivateInteraction(
    private val repository: InteractionRepository,
    private val reminderRepository: ReminderRepository,
    private val addNextReminder: AddNextReminder,
) {

    fun setInteractionIsActive(id: String, isActive: Boolean) = flow {
        repository.setInteractionIsActive(id, isActive)
        repository.getInteraction(id)?.let { interaction ->
            var reminders = reminderRepository.getRemindersByInteraction(interaction.id)

            if (isActive && reminders.none { !it.isCompleted || it.dateTime > Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) }) {
                reminders.maxByOrNull { it.dateTime }?.let {
                    val newReminders = addNextReminder.addNextReminder(it.id)
                    reminders = newReminders?.reminders ?: reminders
                }
            }

            emit(interaction.withReminders(reminders))
        }
    }

}