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
    @Serializable
    @SerialName("reminder")
    data class Reminder(
        val workId: String = randomUUID(),
        val itemId: String,
    ) : NotificationItem

    @Serializable
    @SerialName("push")
    data class Push(
        val title: String,
        val message: String,
    ) : NotificationItem
}