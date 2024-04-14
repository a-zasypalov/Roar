package com.gaoyun.roar

import com.gaoyun.roar.domain.AppPreferencesUseCase
import com.gaoyun.roar.domain.backup.CreateBackupUseCase
import com.gaoyun.roar.domain.backup.ImportBackupUseCase
import com.gaoyun.roar.domain.interaction.ActivateInteraction
import com.gaoyun.roar.domain.interaction.DeactivateInteraction
import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.domain.interaction.InsertInteraction
import com.gaoyun.roar.domain.interaction.InteractionsListBuilder
import com.gaoyun.roar.domain.interaction.RemoveInteraction
import com.gaoyun.roar.domain.interaction_template.GetInteractionTemplate
import com.gaoyun.roar.domain.interaction_template.GetInteractionTemplatesForPetType
import com.gaoyun.roar.domain.interaction_template.InsertInteractionTemplate
import com.gaoyun.roar.domain.interaction_template.RemoveInteractionTemplates
import com.gaoyun.roar.domain.onboarding.CompleteOnboardingUseCase
import com.gaoyun.roar.domain.pet.AddPetUseCase
import com.gaoyun.roar.domain.pet.GetPetBreedsUseCase
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.domain.pet.RemovePetUseCase
import com.gaoyun.roar.domain.pet.SetPetAvatar
import com.gaoyun.roar.domain.reminder.AddNextReminder
import com.gaoyun.roar.domain.reminder.GetReminder
import com.gaoyun.roar.domain.reminder.InsertReminder
import com.gaoyun.roar.domain.reminder.RemoveReminder
import com.gaoyun.roar.domain.reminder.SetReminderComplete
import com.gaoyun.roar.domain.repeat_config.RepeatConfigUseCase
import com.gaoyun.roar.domain.sync.SynchronisationUseCase
import com.gaoyun.roar.domain.user.CheckUserExistingUseCase
import com.gaoyun.roar.domain.user.EditUserUseCase
import com.gaoyun.roar.domain.user.GetCurrentUserUseCase
import com.gaoyun.roar.domain.user.LogoutUseCase
import com.gaoyun.roar.domain.user.RegisterUserUseCase
import com.gaoyun.roar.migrations.MigrationsExecutor
import com.gaoyun.roar.model.entity.RoarDatabase
import com.gaoyun.roar.network.InteractionTemplatesApi
import com.gaoyun.roar.network.PetsApi
import com.gaoyun.roar.network.SynchronisationApi
import com.gaoyun.roar.presentation.onboarding.OnboardingViewModel
import com.gaoyun.roar.repository.InteractionRepository
import com.gaoyun.roar.repository.InteractionRepositoryImpl
import com.gaoyun.roar.repository.InteractionTemplateRepository
import com.gaoyun.roar.repository.InteractionTemplateRepositoryImpl
import com.gaoyun.roar.repository.PetRepository
import com.gaoyun.roar.repository.PetRepositoryImpl
import com.gaoyun.roar.repository.ReminderRepository
import com.gaoyun.roar.repository.ReminderRepositoryImpl
import com.gaoyun.roar.repository.UserRepository
import com.gaoyun.roar.repository.UserRepositoryImpl
import com.gaoyun.roar.ui.AppViewModel
import com.gaoyun.roar.util.DriverFactory
import com.gaoyun.roar.util.PlatformHttpClient
import com.gaoyun.roar.util.Preferences
import com.gaoyun.roar.util.platformModule
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin() = startKoin {
    modules(platformModule(), repositoryModule, useCaseModule, dbModule, networkModule, preferencesModule)
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(platformModule(), repositoryModule, useCaseModule, dbModule, networkModule, preferencesModule)
}

val repositoryModule = module {
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<PetRepository> { PetRepositoryImpl(get(), get(), get(), get(), get()) }
    single<InteractionTemplateRepository> { InteractionTemplateRepositoryImpl(get(), get(), get()) }
    single<InteractionRepository> { InteractionRepositoryImpl(get(), get()) }
    single<ReminderRepository> { ReminderRepositoryImpl(get(), get()) }
}

val useCaseModule = module {
    single { AppViewModel() } //TODO: Refactor to ::AppViewModel
    single { OnboardingViewModel(get()) }

    single { RegisterUserUseCase(get(), get()) }
    single { GetCurrentUserUseCase(get(), get()) }
    single { CheckUserExistingUseCase(get()) }
    single { EditUserUseCase(get()) }
    single { LogoutUseCase(get(), get(), get(), get(), get(), get()) }
    single { SynchronisationUseCase(get(), get(), get(), get(), get(), get(), get(), get(), get()) }

    single { GetPetUseCase(get()) }
    single { AddPetUseCase(get(), get()) }
    single { GetPetBreedsUseCase(get()) }
    single { SetPetAvatar(get()) }
    single { RemovePetUseCase(get(), get()) }

    single { GetInteractionTemplatesForPetType(get()) }
    single { GetInteractionTemplate(get()) }
    single { InsertInteractionTemplate(get()) }
    single { RemoveInteractionTemplates(get()) }

    single { GetInteraction(get(), get()) }
    single { InsertInteraction(get()) }
    single { RemoveInteraction(get(), get(), get()) }
    single { ActivateInteraction(get(), get(), get()) }
    single { RepeatConfigUseCase(get()) }

    single { GetReminder(get()) }
    single { InsertReminder(get(), get()) }
    single { RemoveReminder(get(), get()) }
    single { SetReminderComplete(get(), get(), get(), get(), get(), get()) }
    single { AddNextReminder(get(), get(), get(), get(), get(), get()) }
    single { DeactivateInteraction(get(), get()) }

    single { CreateBackupUseCase(get(), get(), get(), get()) }
    single { ImportBackupUseCase(get(), get(), get(), get(), get(), get(), get()) }
    single { CompleteOnboardingUseCase(get()) }
    single { AppPreferencesUseCase(get()) }

    single { InteractionsListBuilder(get(), get(), get()) }
}

val networkModule = module {
    single { PlatformHttpClient.httpClient() }
    single { InteractionTemplatesApi(get()) }
    single { PetsApi(get()) }
    single { SynchronisationApi() }
}

val dbModule = module {
    single { get<DriverFactory>().createDriver() }
    single { RoarDatabase(get()) }
    single { MigrationsExecutor() }
}

val preferencesModule = module {
    single { Preferences("app_prefs") }
}