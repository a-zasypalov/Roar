package com.gaoyun.roar.android

import android.app.Application
import com.gaoyun.roar.initKoin
import com.gaoyun.roar.presentation.add_pet.avatar.AddPetAvatarScreenViewModel
import com.gaoyun.roar.presentation.add_pet.data.AddPetDataScreenViewModel
import com.gaoyun.roar.presentation.add_pet.setup.AddPetSetupScreenViewModel
import com.gaoyun.roar.presentation.add_pet.type.AddPetPetTypeScreenViewModel
import com.gaoyun.roar.presentation.add_reminder.choose_template.AddReminderScreenViewModel
import com.gaoyun.roar.presentation.add_reminder.setup_reminder.SetupReminderScreenViewModel
import com.gaoyun.roar.presentation.home_screen.HomeScreenViewModel
import com.gaoyun.roar.presentation.pet_screen.PetScreenViewModel
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

    viewModel { AddPetDataScreenViewModel() }
    viewModel { AddPetPetTypeScreenViewModel() }
    viewModel { AddPetAvatarScreenViewModel() }
    viewModel { AddPetSetupScreenViewModel() }

    viewModel { PetScreenViewModel() }

    viewModel { AddReminderScreenViewModel() }
    viewModel { SetupReminderScreenViewModel() }
}