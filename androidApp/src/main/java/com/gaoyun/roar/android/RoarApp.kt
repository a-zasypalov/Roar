package com.gaoyun.roar.android

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.core.app.NotificationManagerCompat
import androidx.work.WorkManager
import com.gaoyun.roar.android.notifications.FcmService
import com.gaoyun.roar.android.notifications.NotificationSchedulerImpl
import com.gaoyun.roar.android.notifications.handling.NotificationChannelProvider
import com.gaoyun.roar.android.notifications.handling.NotificationDisplayingImpl
import com.gaoyun.roar.android.platform_utils.RegistrationLauncherAndroid
import com.gaoyun.roar.domain.NotificationScheduler
import com.gaoyun.roar.domain.SynchronisationScheduler
import com.gaoyun.roar.domain.SynchronisationSchedulerImpl
import com.gaoyun.roar.domain.SynchronisationWorker
import com.gaoyun.roar.initKoin
import com.gaoyun.roar.migrations.MigrationsExecutor
import com.gaoyun.roar.navigation.CloseAppActionHandlerImpl
import com.gaoyun.roar.network.SynchronisationApi
import com.gaoyun.roar.network.SynchronisationApiAndroid
import com.gaoyun.roar.notifications.NotificationDisplaying
import com.gaoyun.roar.ui.features.registration.RegistrationLauncher
import com.gaoyun.roar.ui.navigation.CloseAppActionHandler
import com.gaoyun.roar.util.ActivityProvider
import com.gaoyun.roar.util.BackupHandler
import com.gaoyun.roar.util.BackupHandlerImpl
import com.gaoyun.roar.util.EmailSender
import com.gaoyun.roar.util.EmailSenderImpl
import com.gaoyun.roar.util.SignOutExecutor
import com.gaoyun.roar.util.SignOutExecutorImpl
import com.gaoyun.roar.util.ThemeChanger
import com.gaoyun.roar.util.ThemeChangerAndroid
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.dsl.worker
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.logger.Level
import org.koin.dsl.module

class RoarApp : Application(), KoinComponent {

    private val migrationsExecutor: MigrationsExecutor by inject()
    private val backupHandler: BackupHandler by inject()
    var initialActivity: Activity? = null

    private val appModule = module {
        single<RegistrationLauncher> { RegistrationLauncherAndroid }
        single<SynchronisationApi> { SynchronisationApiAndroid() }
        single<ThemeChanger> { ThemeChangerAndroid(get()) }
        single<BackupHandler> { BackupHandlerImpl(get()) }
        single<SignOutExecutor> { SignOutExecutorImpl() }
        single { ActivityProvider(androidApplication(), initialActivity) }
        single<EmailSender> { EmailSenderImpl(get()) }
        single<CloseAppActionHandler> { CloseAppActionHandlerImpl(get()) }
    }

    private val activityCallback = object : ActivityLifecycleCallbacks {
        override fun onActivityStarted(activity: Activity) {}
        override fun onActivityStopped(activity: Activity) {}
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        override fun onActivityDestroyed(activity: Activity) {
            backupHandler.unregisterExecutor()
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            initialActivity = activity
            backupHandler.registerExecutor()
        }

        override fun onActivityResumed(activity: Activity) {
            initialActivity = activity
        }

        override fun onActivityPaused(activity: Activity) {
            initialActivity = null
        }
    }

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(activityCallback)

        initKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@RoarApp)
            modules(appModule, notificationsModule)
            workManagerFactory()
        }

        migrationsExecutor.migrate()
    }
}

val notificationsModule = module {
    single<NotificationScheduler> { NotificationSchedulerImpl(get(), get()) }
    single<SynchronisationScheduler> { SynchronisationSchedulerImpl() }
    single { WorkManager.getInstance(get()) }
    single { NotificationManagerCompat.from(get()) }
    single { NotificationChannelProvider(get()) }
    single<NotificationDisplaying> { NotificationDisplayingImpl(get(), get(), get()) }
    single { FcmService() }
    worker { NotificationSchedulerImpl.NotificationWorker(get(), get(), get()) }
    worker { SynchronisationWorker(get(), get(), get(), get()) }
}