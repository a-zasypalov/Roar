package com.gaoyun.roar.util

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
}