package com.gaoyun.roar.android

import androidx.core.app.NotificationManagerCompat
import androidx.multidex.MultiDexApplication
import androidx.work.WorkManager
import com.gaoyun.notifications.*
import com.gaoyun.notifications.sync.SynchronisationSchedulerImpl
import com.gaoyun.notifications.sync.SynchronisationWorker
import com.gaoyun.roar.domain.NotificationScheduler
import com.gaoyun.roar.domain.SynchronisationScheduler
import com.gaoyun.roar.initKoin
import com.gaoyun.roar.migrations.MigrationsExecutor
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
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.logger.Level
import org.koin.dsl.module

class RoarApp : MultiDexApplication(), KoinComponent {

    private val migrationsExecutor: MigrationsExecutor by inject()

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@RoarApp)
            modules(appModule, notificationsModule)
            workManagerFactory()
        }

        migrationsExecutor.migrate()
    }
}

val appModule = module {
    viewModel { OnboardingViewModel() }
    viewModel { HomeScreenViewModel() }
    viewModel { RegisterUserViewModel() }
    viewModel { UserScreenViewModel() }
    viewModel { EditUserScreenViewModel() }

    viewModel { AddPetDataScreenViewModel() }
    viewModel { AddPetPetTypeScreenViewModel() }
    viewModel { AddPetAvatarScreenViewModel() }
    viewModel { AddPetSetupScreenViewModel() }

    viewModel { PetScreenViewModel() }
    viewModel { InteractionScreenViewModel() }

    viewModel { AddReminderScreenViewModel() }
    viewModel { SetupReminderScreenViewModel() }
    viewModel { AddReminderCompleteScreenViewModel() }
}

val notificationsModule = module {
    single<NotificationScheduler> { NotificationSchedulerImpl(get(), get()) }
    single<SynchronisationScheduler> { SynchronisationSchedulerImpl(get()) }
    single<NotificationIntentProvider> { NotificationIntentProviderImpl() }
    single { WorkManager.getInstance(get()) }
    single { NotificationManagerCompat.from(get()) }
    single { NotificationChannelProvider(get()) }
    single { NotificationDisplayer(get(), get(), get()) }
    single { NotificationHandler(get(), get(), get(), get()) }
    single { FcmService() }
    worker { NotificationWorker(get(), get()) }
    worker { SynchronisationWorker(get(), get()) }
}