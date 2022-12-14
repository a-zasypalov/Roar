package com.gaoyun.roar.android

import android.app.Application
import com.gaoyun.roar.initKoin
import com.gaoyun.roar.presentation.add_pet.AddPetScreenViewModel
import com.gaoyun.roar.presentation.add_reminder.AddReminderScreenViewModel
import com.gaoyun.roar.presentation.home_screen.HomeScreenViewModel
import com.gaoyun.roar.presentation.user_register.RegisterUserViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.logger.Level
import org.koin.dsl.module

class RoarApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@RoarApp)
            modules(appModule)
        }
    }
}

val appModule = module {
    viewModel { HomeScreenViewModel() }
    viewModel { RegisterUserViewModel() }
    viewModel { AddPetScreenViewModel() }
    viewModel { AddReminderScreenViewModel() }
}