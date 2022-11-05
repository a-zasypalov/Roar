package com.gaoyun.roar.domain.reminder

import com.gaoyun.roar.repository.ReminderRepository
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SetReminderIsActive : KoinComponent {

    private val repository: ReminderRepository by inject()

    fun activateReminder(id: String) = flow { emit(repository.setReminderIsActive(id, true)) }

    fun deactivateReminder(id: String) = flow { emit(repository.setReminderIsActive(id, false)) }

}