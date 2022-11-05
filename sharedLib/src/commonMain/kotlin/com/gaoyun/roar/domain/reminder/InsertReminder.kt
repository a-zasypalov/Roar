package com.gaoyun.roar.domain.reminder

import com.gaoyun.roar.model.domain.Reminder
import com.gaoyun.roar.repository.ReminderRepository
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class InsertReminder : KoinComponent {

    private val repository: ReminderRepository by inject()

    fun insertReminder(interactionId: String, dateTime: LocalDateTime) = flow {
        emit(
            repository.insertReminder(
                Reminder(
                    interactionId = interactionId,
                    dateTime = dateTime,
                )
            )
        )
    }

}