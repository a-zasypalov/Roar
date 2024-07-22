package com.gaoyun.roar

import com.gaoyun.roar.domain.NotificationScheduler
import com.gaoyun.roar.domain.SynchronisationScheduler
import com.gaoyun.roar.domain.user.DeleteRemoteAccountExecutor
import com.gaoyun.roar.network.SynchronisationApi
import com.gaoyun.roar.ui.features.registration.RegistrationLauncher
import com.gaoyun.roar.util.EmailSender
import com.gaoyun.roar.util.SignOutExecutor
import com.gaoyun.roar.util.ThemeChanger

class iOSAppDeclaration(
    val registrationLauncher: RegistrationLauncher,
    val synchronisationApi: SynchronisationApi,
    val synchronisationScheduler: SynchronisationScheduler,
    val themeChanger: ThemeChanger,
    val notificationScheduler: NotificationScheduler,
    val signOutExecutor: SignOutExecutor,
    val emailSender: EmailSender,
    val deleteRemoteAccountExecutor: DeleteRemoteAccountExecutor
)