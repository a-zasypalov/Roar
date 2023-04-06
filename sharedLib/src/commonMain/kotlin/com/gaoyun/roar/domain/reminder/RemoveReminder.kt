package com.gaoyun.roar.domain.reminder

import com.gaoyun.roar.domain.NotificationScheduler
import com.gaoyun.roar.repository.ReminderRepository
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RemoveReminder : KoinComponent {

    private val repository: ReminderRepository by inject()
    private val notificationScheduler: NotificationScheduler by inject()

    fun removeReminder(id: String) = flow {
        repository.getReminder(id)?.notificationJobId?.let { notificationScheduler.cancelNotification(it) }
        emit(repository.deleteReminder(id))
    }

    fun removeReminderByInteraction(interactionId: String, scheduleSync: Boolean = true) = flow {
        repository.getRemindersByInteraction(interactionId)
            .mapNotNull { it.notificationJobId }
            .forEach { notificationScheduler.cancelNotification(it) }

        emit(repository.deleteReminderByInteractionId(interactionId, scheduleSync))
    }


}