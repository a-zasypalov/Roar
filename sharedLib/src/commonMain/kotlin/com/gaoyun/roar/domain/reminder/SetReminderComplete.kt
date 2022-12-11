package com.gaoyun.roar.domain.reminder

import com.gaoyun.roar.repository.ReminderRepository
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SetReminderComplete : KoinComponent {

    private val repository: ReminderRepository by inject()

    fun complete(id: String) = flow {
        emit(repository.setReminderCompleted(id, true))
    }

    fun uncomplete(id: String) = flow {
        emit(repository.setReminderCompleted(id, false))
    }

    private fun addNextReminder(reminderId: String) {

    }

}