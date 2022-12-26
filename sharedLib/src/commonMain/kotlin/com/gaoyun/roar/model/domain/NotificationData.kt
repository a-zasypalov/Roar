package com.gaoyun.roar.model.domain

import com.gaoyun.roar.util.randomUUID
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationData(
    val item: NotificationItem,
    val scheduled: LocalDateTime,
)

@Serializable
sealed interface NotificationItem {
    val workId: String
    val itemId: String

    @Serializable
    @SerialName("reminder")
    data class Reminder(
        override val workId: String = randomUUID(),
        override val itemId: String
    ) : NotificationItem
}