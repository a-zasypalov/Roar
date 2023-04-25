package com.gaoyun.notifications

import android.content.Context
import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.model.domain.NotificationData
import com.gaoyun.roar.model.domain.NotificationItem
import kotlinx.coroutines.flow.firstOrNull
import org.koin.core.component.KoinComponent
import com.gaoyun.common.R as CommonR

class NotificationHandler(
    private val displayer: NotificationDisplayer,
    private val getInteraction: GetInteraction,
    private val getPetUseCase: GetPetUseCase,
    private val notificationIntentProvider: NotificationIntentProvider,
    private val context: Context,
) : KoinComponent {

    suspend fun handle(notification: NotificationData): Boolean {
        return when (notification.item) {
            is NotificationItem.Reminder -> {
                val item = notification.item as NotificationItem.Reminder
                val interaction = getInteraction.getInteractionByReminder(item.itemId).firstOrNull() ?: return false
                val pet = getPetUseCase.getPet(interaction.petId).firstOrNull() ?: return false

                displayer.display(
                    title = context.getString(CommonR.string.notification_title, pet.name),
                    content = context.getString(CommonR.string.notification_content_dont_forget, interaction.name.lowercase()),
                    channel = NotificationChannel.PetsReminder,
                    intent = notificationIntentProvider.getDefaultIntent()
                )
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