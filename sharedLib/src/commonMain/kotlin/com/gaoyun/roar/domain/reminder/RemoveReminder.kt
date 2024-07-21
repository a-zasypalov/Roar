package com.gaoyun.roar.domain.reminder

import com.gaoyun.roar.domain.NotificationScheduler
import com.gaoyun.roar.repository.ReminderRepository
import kotlinx.coroutines.flow.flow

class RemoveReminder(
    private val repository: ReminderRepository,
    private val notificationScheduler: NotificationScheduler,
) {

    fun removeReminder(id: String) = flow {
        repository.getReminder(id)?.notificationJobId?.let { notificationScheduler.cancelNotification(it) }
        emit(repository.deleteReminder(id))
    }

    fun removeReminderByInteraction(interactionId: String, scheduleSync: Boolean = true, removeNotification: Boolean = true) = flow {
        repository.getRemindersByInteraction(interactionId)
            .mapNotNull { it.notificationJobId }
            .forEach { if(removeNotification) notificationScheduler.cancelNotification(it) }

        emit(repository.deleteReminderByInteractionId(interactionId, scheduleSync))
    }


}