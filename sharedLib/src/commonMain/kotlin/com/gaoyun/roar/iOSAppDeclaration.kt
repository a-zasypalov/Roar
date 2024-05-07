package com.gaoyun.roar

import com.gaoyun.roar.domain.SynchronisationScheduler
import com.gaoyun.roar.network.SynchronisationApi
import com.gaoyun.roar.ui.features.registration.RegistrationLauncher

class iOSAppDeclaration(
    val registrationLauncher: RegistrationLauncher,
    val synchronisationApi: SynchronisationApi,
    val synchronisationScheduler: SynchronisationScheduler
)