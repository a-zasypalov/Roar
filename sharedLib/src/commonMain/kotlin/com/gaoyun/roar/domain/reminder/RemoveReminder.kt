package com.gaoyun.roar.domain.reminder

import com.gaoyun.roar.repository.ReminderRepository
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RemoveReminder : KoinComponent {

    private val repository: ReminderRepository by inject()

    fun removeReminder(id: String) = flow { emit(repository.deleteReminder(id)) }

    fun removeReminderByInteraction(interactionId: String) = flow { emit(repository.deleteReminderByInteractionId(interactionId)) }


}