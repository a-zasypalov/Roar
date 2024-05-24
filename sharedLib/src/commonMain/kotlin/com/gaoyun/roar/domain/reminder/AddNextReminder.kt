package com.gaoyun.roar.domain.reminder

import com.gaoyun.roar.domain.NotificationScheduler
import com.gaoyun.roar.domain.interaction.DeactivateInteraction
import com.gaoyun.roar.domain.interaction.GetInteraction
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

class AddNextReminder(
    private val insertReminder: InsertReminder,
    private val repeatConfigUseCase: RepeatConfigUseCase,
    private val deactivateInteraction: DeactivateInteraction,
    private val notificationScheduler: NotificationScheduler,
    private val getInteraction: GetInteraction,
    private val getReminder: GetReminder,
) {

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
                val newNotificationData = prepareNextReminder(
                    dateTime = nextReminderDateTime,
                    reminderId = newReminderId,
                    remindConfig = interaction.remindConfig,
                )
                val newNotificationJobId = (newNotificationData.item as? NotificationItem.Reminder)?.workId
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
                    deactivateInteraction.deactivate(interaction.id).firstOrNull()
                }

                scheduleNextReminder(newNotificationData)

            } ?: deactivateInteraction.deactivate(interaction.id).firstOrNull()
        }

        return getNewInteractionState(interaction.id)
    }

    private fun scheduleNextReminder(notificationData: NotificationData) {
        notificationScheduler.scheduleNotification(notificationData)
    }

    private fun prepareNextReminder(dateTime: LocalDateTime, reminderId: String, remindConfig: InteractionRemindConfig): NotificationData {
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

        return notificationData
    }

    private suspend fun getNewInteractionState(interactionId: String) = getInteraction.getInteractionWithReminders(interactionId).firstOrNull()
}