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

interface IdentifiableNotification {
    val workId: String
}

@Serializable
sealed interface NotificationItem {
    @Serializable
    @SerialName("reminder")
    data class Reminder(
        override val workId: String = randomUUID(),
        val itemId: String,
    ) : NotificationItem, IdentifiableNotification

    @Serializable
    @SerialName("push")
    data class Push(
        val title: String,
        val message: String,
    ) : NotificationItem

    @Serializable
    @SerialName("info_reminder")
    data class InfoReminder(
        //Static ID do not change
        override val workId: String = "97125f96-fcc6-4ea1-8c53-4ab97babd8cc",
    ) : NotificationItem, IdentifiableNotification
}