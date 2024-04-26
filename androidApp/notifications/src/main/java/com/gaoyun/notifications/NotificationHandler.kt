package com.gaoyun.notifications

import android.content.Context
import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.domain.reminder.GetReminder
import com.gaoyun.roar.model.domain.NotificationData
import com.gaoyun.roar.model.domain.NotificationItem
import kotlinx.coroutines.flow.firstOrNull

class NotificationHandler(
    private val displayer: NotificationDisplayer,
    private val getInteraction: GetInteraction,
    private val getReminder: GetReminder,
    private val getPetUseCase: GetPetUseCase,
    private val notificationIntentProvider: NotificationIntentProvider,
    private val context: Context,
) {

    suspend fun handle(notification: NotificationData): Boolean {
        return when (notification.item) {
            is NotificationItem.Reminder -> {
                val item = notification.item as NotificationItem.Reminder
                val reminder = getReminder.getReminder(item.itemId).firstOrNull() ?: return false
                val interaction = getInteraction.getInteractionByReminder(item.itemId).firstOrNull() ?: return false
                val pet = getPetUseCase.getPet(interaction.petId).firstOrNull() ?: return false

                val reminderIsCompleted = reminder.isCompleted
                if (reminderIsCompleted.not()) {
                    displayer.display(
                        title = "Notification title", // TODO: fix context.getString(CommonR.string.notification_title, pet.name),
                        content = "Notification text",
//                        context.getString(
//                            CommonR.string.notification_content_dont_forget,
//                            interaction.name,
//                            reminder.dateTime.toJavaLocalDateTime().format(DateUtils.ddMmmmDateFormatter)
//                        ),
                        channel = NotificationChannel.PetsReminder,
                        intent = notificationIntentProvider.getDefaultIntent()
                    )
                }

                true
            }

            is NotificationItem.Push -> {
                handleImmediate(notification.item as NotificationItem.Push)
                true
            }
        }
    }

    fun handleImmediate(notification: NotificationItem.Push) {
        displayer.display(
            title = notification.title,
            content = notification.message,
            channel = NotificationChannel.PetsReminder,
            intent = notificationIntentProvider.getDefaultIntent()
        )
    }

}