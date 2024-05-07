package com.gaoyun.roar

import com.gaoyun.roar.domain.backup.CreateBackupUseCase
import com.gaoyun.roar.domain.sync.SynchronisationUseCase
import com.gaoyun.roar.network.SynchronisationApi
import com.gaoyun.roar.util.Preferences
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class KoinProvider: KoinComponent {
    val preferences: Preferences by inject()
    val synchronisationUseCase: SynchronisationUseCase by inject()
    val createBackupUseCase: CreateBackupUseCase by inject()
    val synchronisationApi: SynchronisationApi by inject()
}