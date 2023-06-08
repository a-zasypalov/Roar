package com.gaoyun.roar.domain.reminder

import com.gaoyun.roar.repository.ReminderRepository
import kotlinx.coroutines.flow.flow

class GetReminder(
    private val repository: ReminderRepository,
) {

    fun getReminder(id: String) = flow { emit(repository.getReminder(id)) }
    fun getReminderByInteraction(interactionId: String) = flow { emit(repository.getRemindersByInteraction(interactionId)) }

}