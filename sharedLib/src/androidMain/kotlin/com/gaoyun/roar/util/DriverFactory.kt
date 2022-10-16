package com.gaoyun.roar.util

import android.content.Context
import com.gaoyun.roar.model.entity.RoarDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.koin.core.module.Module
import org.koin.dsl.module

actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(RoarDatabase.Schema, context, "roar.db")
    }
}

actual fun platformModule(): Module = module {
    single { DriverFactory(get()) }
}