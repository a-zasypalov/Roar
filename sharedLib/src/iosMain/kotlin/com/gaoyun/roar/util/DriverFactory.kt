package com.gaoyun.roar.util

import com.gaoyun.roar.model.entity.RoarDatabase
import com.gaoyun.roar.presentation.add_pet.data.AddPetDataScreenViewModel
import com.gaoyun.roar.presentation.add_reminder.choose_template.AddReminderScreenViewModel
import com.gaoyun.roar.presentation.home_screen.HomeScreenViewModel
import com.gaoyun.roar.presentation.user_register.RegisterUserViewModel
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import org.koin.core.module.Module
import org.koin.dsl.module

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(RoarDatabase.Schema, "roar.db")
    }
}

actual fun platformModule(): Module = module {
    single { DriverFactory() }

    factory { HomeScreenViewModel() }
    factory { AddPetDataScreenViewModel() }
    factory { AddReminderScreenViewModel() }
    factory { RegisterUserViewModel() }

}