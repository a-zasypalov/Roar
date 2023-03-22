package com.gaoyun.roar.migrations

import com.gaoyun.roar.model.entity.RoarDatabase
import com.squareup.sqldelight.db.AfterVersion
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.db.migrateWithCallbacks
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MigrationsExecutor : KoinComponent {

    private val driver: SqlDriver by inject()

    fun migrate() {
            try {
                RoarDatabase.Schema.migrateWithCallbacks(
                    driver = driver,
                    oldVersion = 0,
                    newVersion = RoarDatabase.Schema.version,
                    AfterVersion(2) {
                        driver.execute(
                            identifier = null,
                            sql = "ALTER TABLE InteractionEntity ADD COLUMN remindConfig TEXT DEFAULT NULL;",
                            parameters = 0
                        )
                    },
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
    }

    private val migrationTasks = listOf(
        {
            RoarDatabase.Schema.migrateWithCallbacks(
                driver = driver,
                oldVersion = 0,
                newVersion = RoarDatabase.Schema.version,
                AfterVersion(0) {
                    driver.execute(
                        identifier = null,
                        sql = "ALTER TABLE ReminderEntity ADD COLUMN notificationJobId TEXT;",
                        parameters = 0
                    )
                },
            )
        }
    )

}