package com.gaoyun.roar.util

import com.squareup.sqldelight.db.SqlDriver
import org.koin.core.module.Module

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

expect fun platformModule(): Module

