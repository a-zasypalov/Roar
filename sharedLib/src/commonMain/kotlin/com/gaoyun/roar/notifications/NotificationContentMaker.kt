package com.gaoyun.roar.notifications

import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.domain.reminder.GetReminder
import com.gaoyun.roar.util.DateFormats
import com.gaoyun.roar.util.formatDateTime
import kotlinx.coroutines.flow.firstOrNull
import org.jetbrains.compose.resources.getString
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.notification_content_dont_forget
import roar.sharedlib.generated.resources.notification_title

class NotificationContentMaker(
    private val getInteraction: GetInteraction,
    private val getReminder: GetReminder,
    private val getPetUseCase: GetPetUseCase,
) {
    suspend fun make(itemId: String): NotificationContent? {
        val reminder = getReminder.getReminder(itemId).firstOrNull() ?: return null
        val interaction = getInteraction.getInteractionByReminder(itemId).firstOrNull() ?: return null
        val pet = getPetUseCase.getPet(interaction.petId).firstOrNull() ?: return null

        return if (reminder.isCompleted.not()) {
            NotificationContent(
                title = getString(Res.string.notification_title, pet.name),
                content = getString(
                    Res.string.notification_content_dont_forget,
                    interaction.name,
                    reminder.dateTime.formatDateTime(DateFormats.ddMmmmDateFormat)
                ),
            )
        } else null
    }
}

data class NotificationContent(
    val title: String,
    val content: String,
)