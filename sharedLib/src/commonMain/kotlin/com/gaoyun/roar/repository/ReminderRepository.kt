package com.gaoyun.roar.repository

import com.gaoyun.roar.model.domain.Reminder
import com.gaoyun.roar.model.domain.toDomain
import com.gaoyun.roar.model.entity.RoarDatabase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface ReminderRepository {
    fun insertReminder(reminder: Reminder)
    fun getReminder(id: String): Reminder?
    fun getRemindersByInteraction(interactionId: String): List<Reminder>
    fun setReminderIsActive(reminderId: String, isActive: Boolean)
    fun deleteReminder(id: String)
}

class ReminderRepositoryImpl : ReminderRepository, KoinComponent {

    private val appDb: RoarDatabase by inject()

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
            isCompleted = reminder.isCompleted
        )
    }

    override fun setReminderIsActive(reminderId: String, isActive: Boolean) {
        appDb.reminderEntityQueries.setIsCompleted(reminderId, isActive)
    }

    override fun deleteReminder(id: String) {
        appDb.reminderEntityQueries.deleteReminderById(id)
    }
}