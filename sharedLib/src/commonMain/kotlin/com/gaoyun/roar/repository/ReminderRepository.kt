package com.gaoyun.roar.repository

import com.gaoyun.roar.domain.SynchronisationScheduler
import com.gaoyun.roar.model.domain.Reminder
import com.gaoyun.roar.model.domain.toDomain
import com.gaoyun.roar.model.entity.RoarDatabase
import kotlinx.datetime.LocalDateTime

interface ReminderRepository {
    fun insertReminder(reminder: Reminder)
    fun getReminder(id: String): Reminder?
    fun getRemindersByInteraction(interactionId: String): List<Reminder>
    fun setReminderCompleted(reminderId: String, complete: Boolean, completionDateTime: LocalDateTime)
    fun deleteReminder(id: String)
    fun deleteReminderByInteractionId(interactionId: String, scheduleSync: Boolean = true)
}

class ReminderRepositoryImpl(
    private val appDb: RoarDatabase,
    private val scheduler: SynchronisationScheduler,
) : ReminderRepository {

    override fun getReminder(id: String): Reminder? {
        return appDb.reminderEntityQueries.selectById(id).executeAsOneOrNull()?.toDomain()
    }

    override fun getRemindersByInteraction(interactionId: String): List<Reminder> {
        return appDb.reminderEntityQueries.selectByInteraction(interactionId).executeAsList().map { it.toDomain() }
    }

    override fun insertReminder(reminder: Reminder) {
        appDb.reminderEntityQueries.insertReminder(
            id = reminder.id,
            interactionId = reminder.interactionId,
            dateTime = reminder.dateTime.toString(),
            isCompleted = reminder.isCompleted,
            notificationJobId = reminder.notificationJobId
        )
        scheduler.scheduleSynchronisation()
    }

    override fun setReminderCompleted(reminderId: String, complete: Boolean, completionDateTime: LocalDateTime) {
        val reminder = getReminder(reminderId) ?: return
        insertReminder(reminder.copy(isCompleted = complete, dateTime = completionDateTime))
        scheduler.scheduleSynchronisation()
    }

    override fun deleteReminder(id: String) {
        appDb.reminderEntityQueries.deleteReminderById(id)
        scheduler.scheduleSynchronisation()
    }

    override fun deleteReminderByInteractionId(interactionId: String, scheduleSync: Boolean) {
        appDb.reminderEntityQueries.deleteReminderByInteractionId(interactionId)
        if (scheduleSync) scheduler.scheduleSynchronisation()
    }
}