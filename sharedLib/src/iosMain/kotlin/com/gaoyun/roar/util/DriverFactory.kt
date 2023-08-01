package com.gaoyun.roar.util

import com.gaoyun.roar.domain.SynchronisationScheduler
import com.gaoyun.roar.domain.SynchronisationSchedulerImpl
import com.gaoyun.roar.model.entity.RoarDatabase
import com.gaoyun.roar.presentation.about_screen.AboutScreenViewModel
import com.gaoyun.roar.presentation.add_pet.avatar.AddPetAvatarScreenViewModel
import com.gaoyun.roar.presentation.add_pet.data.AddPetDataScreenViewModel
import com.gaoyun.roar.presentation.add_pet.setup.AddPetSetupScreenViewModel
import com.gaoyun.roar.presentation.add_pet.type.AddPetPetTypeScreenViewModel
import com.gaoyun.roar.presentation.add_reminder.choose_template.AddReminderScreenViewModel
import com.gaoyun.roar.presentation.add_reminder.complete.AddReminderCompleteScreenViewModel
import com.gaoyun.roar.presentation.add_reminder.setup_reminder.SetupReminderScreenViewModel
import com.gaoyun.roar.presentation.home_screen.HomeScreenViewModel
import com.gaoyun.roar.presentation.interactions.InteractionScreenViewModel
import com.gaoyun.roar.presentation.onboarding.OnboardingViewModel
import com.gaoyun.roar.presentation.pet_screen.PetScreenViewModel
import com.gaoyun.roar.presentation.user_edit.EditUserScreenViewModel
import com.gaoyun.roar.presentation.user_register.RegisterUserViewModel
import com.gaoyun.roar.presentation.user_screen.UserScreenViewModel
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.dsl.module

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(RoarDatabase.Schema, "roar.db")
    }
}

actual fun platformModule() = module {
    single { DriverFactory() }
    single<SynchronisationScheduler> { SynchronisationSchedulerImpl() }

    single { OnboardingViewModel(get()) }
    single { HomeScreenViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    single { RegisterUserViewModel(get(), get()) }
    single { UserScreenViewModel(get(), get(), get(), get(), get(), get(), get()) }
    single { EditUserScreenViewModel(get(), get()) }

    single { AddPetDataScreenViewModel(get(), get(), get(), get()) }
    single { AddPetPetTypeScreenViewModel() }
    single { AddPetAvatarScreenViewModel(get()) }
    single { AddPetSetupScreenViewModel(get()) }

    single { PetScreenViewModel(get(), get(), get(), get(), get()) }
    single { InteractionScreenViewModel(get(), get(), get(), get(), get(), get(), get()) }

    single { AddReminderScreenViewModel(get(), get(), get()) }
    single { SetupReminderScreenViewModel(get(), get(), get(), get(), get(), get()) }
    single { AddReminderCompleteScreenViewModel() }

    single { AboutScreenViewModel() }
}

object ViewModelProvider : KoinComponent {
    fun onboardingViewModel() = get<OnboardingViewModel>()
    fun registerUserViewModel() = get<RegisterUserViewModel>()
    fun homeScreenViewModel() = get<HomeScreenViewModel>()
    fun userScreenViewModel() = get<UserScreenViewModel>()
    fun editUserScreenViewModel() = get<EditUserScreenViewModel>()

    fun addPetDataScreenViewModel() = get<AddPetDataScreenViewModel>()
    fun addPetTypeScreenViewModel() = get<AddPetPetTypeScreenViewModel>()
    fun addPetAvatarScreenViewModel() = get<AddPetAvatarScreenViewModel>()
    fun addPetSetupScreenViewModel() = get<AddPetSetupScreenViewModel>()

    fun petScreenViewModel() = get<PetScreenViewModel>()
    fun interactionScreenViewModel() = get<InteractionScreenViewModel>()

    fun addReminderScreenViewModel() = get<AddReminderScreenViewModel>()
    fun setupReminderScreenViewModel() = get<SetupReminderScreenViewModel>()
    fun addReminderCompleteScreenViewModel() = get<AddReminderCompleteScreenViewModel>()

    fun aboutScreenViewModel() = get<AboutScreenViewModel>()
}