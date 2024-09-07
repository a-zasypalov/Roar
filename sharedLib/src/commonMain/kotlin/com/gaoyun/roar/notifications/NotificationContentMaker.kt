package com.gaoyun.roar.notifications

import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.domain.reminder.GetReminder
import com.gaoyun.roar.util.DateFormats
import com.gaoyun.roar.util.formatDateTime
import kotlinx.coroutines.flow.firstOrNull
import org.jetbrains.compose.resources.getPluralString
import org.jetbrains.compose.resources.getString
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.notification_content_dont_forget
import roar.sharedlib.generated.resources.notification_title
import roar.sharedlib.generated.resources.scheduled_info_notification_content
import roar.sharedlib.generated.resources.scheduled_info_notification_title_multiple_pets
import roar.sharedlib.generated.resources.scheduled_info_notification_title_single_pet

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
                    reminder.dateTime.formatDateTime(DateFormats.DD_MMMM_DATE_FORMAT)
                ),
            )
        } else null
    }

    suspend fun makeInfoNotification(petRemindersCount: Map<String, Int>): NotificationContent {
        val remindersCount = petRemindersCount.values.sum()
        return if (petRemindersCount.keys.size > 1) {
            NotificationContent(
                title = getString(Res.string.scheduled_info_notification_title_multiple_pets),
                content = getPluralString(Res.plurals.scheduled_info_notification_content, remindersCount, remindersCount),
            )
        } else {
            val petName = petRemindersCount.keys.firstOrNull() ?: ""
            NotificationContent(
                title = getString(Res.string.scheduled_info_notification_title_single_pet, petName),
                content = getPluralString(Res.plurals.scheduled_info_notification_content, remindersCount, remindersCount),
            )
        }
    }
}

data class NotificationContent(
    val title: String,
    val content: String,
)