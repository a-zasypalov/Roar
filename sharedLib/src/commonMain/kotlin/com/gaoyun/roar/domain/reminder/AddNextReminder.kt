package com.gaoyun.roar.domain.reminder

import com.gaoyun.roar.domain.NotificationScheduler
import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.domain.interaction.SetInteractionIsActive
import com.gaoyun.roar.domain.repeat_config.RepeatConfigUseCase
import com.gaoyun.roar.model.domain.NotificationData
import com.gaoyun.roar.model.domain.NotificationItem
import com.gaoyun.roar.model.domain.Reminder
import com.gaoyun.roar.model.domain.interactions.InteractionRemindConfig
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.util.randomUUID
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AddNextReminder : KoinComponent {
    private val getInteraction: GetInteraction by inject()
    private val getReminder: GetReminder by inject()
    private val insertReminder: InsertReminder by inject()
    private val repeatConfigUseCase: RepeatConfigUseCase by inject()
    private val setInteractionIsActive: SetInteractionIsActive by inject()
    private val notificationScheduler: NotificationScheduler by inject()


    suspend fun addNextReminder(reminderId: String): InteractionWithReminders? {
        val completedReminder = getReminder.getReminder(reminderId).firstOrNull() ?: return null
        notificationScheduler.cancelNotification(completedReminder.notificationJobId)

        val interaction = getInteraction.getInteraction(completedReminder.interactionId).firstOrNull() ?: return null

        if (interaction.repeatConfig != null && interaction.isActive) {
            repeatConfigUseCase.getNextDateAccordingToRepeatConfig(
                repeatConfig = interaction.repeatConfig,
                interactionId = interaction.id,
                from = completedReminder.dateTime.date
            )?.atTime(completedReminder.dateTime.hour, completedReminder.dateTime.minute)?.let { nextReminderDateTime ->
                val newReminderId = randomUUID()
                val newNotificationJobId = scheduleNextReminder(
                    dateTime = nextReminderDateTime,
                    reminderId = newReminderId,
                    remindConfig = interaction.remindConfig,
                )
                val newReminder = Reminder(
                    id = newReminderId,
                    interactionId = interaction.id,
                    dateTime = nextReminderDateTime,
                    notificationJobId = newNotificationJobId
                )

                insertReminder.insertReminder(newReminder).firstOrNull()

                val nextReminderAfterNewOne = repeatConfigUseCase.getNextDateAccordingToRepeatConfig(
                    repeatConfig = interaction.repeatConfig,
                    interactionId = interaction.id,
                    from = completedReminder.dateTime.date
                )
                if (nextReminderAfterNewOne == null) {
                    // Deactivate reminder since next occurrence is the last
                    setInteractionIsActive.setInteractionIsActive(interaction.id, isActive = false).firstOrNull()
                }

            } ?: setInteractionIsActive.setInteractionIsActive(interaction.id, isActive = false).firstOrNull()
        }

        return getNewInteractionState(interaction.id)
    }

    private fun scheduleNextReminder(dateTime: LocalDateTime, reminderId: String, remindConfig: InteractionRemindConfig): String {
        val notificationDateTime = dateTime
            .toInstant(TimeZone.currentSystemDefault())
            .minus(remindConfig.toDuration())
            .toLocalDateTime(TimeZone.currentSystemDefault())

        val notificationData = NotificationData(
            scheduled = notificationDateTime,
            item = NotificationItem.Reminder(
                itemId = reminderId
            )
        )
        notificationScheduler.scheduleNotification(notificationData)

        return (notificationData.item as? NotificationItem.Reminder)?.workId ?: randomUUID()
    }

    private suspend fun getNewInteractionState(interactionId: String) = getInteraction.getInteractionWithReminders(interactionId).firstOrNull()
}