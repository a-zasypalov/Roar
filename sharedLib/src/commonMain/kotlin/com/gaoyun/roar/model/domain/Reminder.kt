package com.gaoyun.roar.model.domain

import com.gaoyun.roar.model.entity.ReminderEntity
import com.gaoyun.roar.util.randomUUID
import kotlinx.datetime.LocalDateTime

data class Reminder(
    val id: String = randomUUID(),
    val interactionId: String,
    val dateTime: LocalDateTime,
    val isCompleted: Boolean = false,
    val notificationJobId: String? = null
)

internal fun ReminderEntity.toDomain(): Reminder {
    return Reminder(
        id = id,
        interactionId = interactionId,
        dateTime = LocalDateTime.parse(dateTime),
        isCompleted = isCompleted,
        notificationJobId = notificationJobId
    )
}
