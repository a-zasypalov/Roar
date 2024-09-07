package com.gaoyun.roar.notifications

import com.gaoyun.roar.domain.AppPreferencesUseCase
import com.gaoyun.roar.model.domain.NotificationData
import com.gaoyun.roar.model.domain.NotificationItem
import com.gaoyun.roar.util.toLocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class ScheduledInfoNotificationCreator(
    private val appPreferencesUseCase: AppPreferencesUseCase
) {
    fun createScheduledInfoNotification(petRemindersCount: Map<String, Int>): NotificationData? {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val lastScheduledInfoNotification = Instant.fromEpochMilliseconds(appPreferencesUseCase.getLastScheduledInfoNotification()).toLocalDate()

        //Show ScheduledInfoNotification not more than once per day
        if (now.date > lastScheduledInfoNotification) {
            val scheduleTime = when {
                now.hour in 10..14 -> now.date.atTime(hour = 15, minute = 0)
                now.hour < 17 -> now.date.atTime(hour = 17, minute = 0)
                now.hour < 19 -> now.date.atTime(hour = 19, minute = 0)
                else -> now.date.plus(1, DateTimeUnit.DAY).atTime(hour = 9, minute = 0)
            }
            appPreferencesUseCase.setLastScheduledInfoNotification(at = scheduleTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds())
            return NotificationData(
                scheduled = scheduleTime,
                item = NotificationItem.InfoReminder(petRemindersCount = petRemindersCount)
            )
        } else return null
    }
}