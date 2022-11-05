package com.gaoyun.roar.domain.reminder

import com.gaoyun.roar.repository.ReminderRepository
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetReminder : KoinComponent {

    private val repository: ReminderRepository by inject()

    fun getReminder(id: String) = flow { emit(repository.getReminder(id)) }

    fun getReminderByInteraction(interactionId: String) = flow { emit(repository.getRemindersByInteraction(interactionId)) }

}