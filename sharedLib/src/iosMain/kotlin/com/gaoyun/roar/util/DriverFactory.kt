package com.gaoyun.roar.util

import com.gaoyun.roar.domain.NotificationScheduler
import com.gaoyun.roar.model.domain.NotificationData
import com.gaoyun.roar.model.entity.RoarDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import org.koin.dsl.module

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(RoarDatabase.Schema, "roar.db")
    }
}

actual fun platformModule() = module {
    single { DriverFactory() }
    single<NotificationScheduler> { NotificationSchedulerImpl() } //TODO: Change to real or remove
}

class NotificationSchedulerImpl : NotificationScheduler {
    override fun scheduleNotification(data: NotificationData) {}
    override fun cancelNotification(id: String?) {}
}