package com.gaoyun.roar.migrations

import com.gaoyun.roar.model.entity.RoarDatabase
import com.squareup.sqldelight.db.SqlDriver
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MigrationsExecutor : KoinComponent {

    private val driver: SqlDriver by inject()

    fun migrate() {
        RoarDatabase.Schema.migrate(
            driver = driver,
            oldVersion = 2,
            newVersion = RoarDatabase.Schema.version,
        )
    }

}