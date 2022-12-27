package com.gaoyun.notifications

import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.model.domain.NotificationData
import com.gaoyun.roar.model.domain.NotificationItem
import kotlinx.coroutines.flow.firstOrNull
import org.koin.core.component.KoinComponent

class NotificationHandler(
    private val displayer: NotificationDisplayer,
    private val getInteraction: GetInteraction,
    private val getPetUseCase: GetPetUseCase,
    private val notificationIntentProvider: NotificationIntentProvider
) : KoinComponent {

    suspend fun handle(notification: NotificationData): Boolean {
        if (notification.item is NotificationItem.Reminder) {
            val interaction = getInteraction.getInteractionByReminder(notification.item.itemId).firstOrNull() ?: return false
            val pet = getPetUseCase.getPet(interaction.petId).firstOrNull() ?: return false

            displayer.display(
                title = interaction.name,
                content = "It's time for ${pet.name} to have a ${interaction.type}",
                channel = NotificationChannel.PetsReminder,
                intent = notificationIntentProvider.getDefaultIntent()
            )
            return true
        }

        return false
    }


}