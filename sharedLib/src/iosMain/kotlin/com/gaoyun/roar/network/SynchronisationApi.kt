package com.gaoyun.roar.network

import org.koin.core.component.KoinComponent

actual class SynchronisationApi : KoinComponent {

    actual fun sendBackup(backup: String) {
    }

    actual suspend fun retrieveBackup(onFinish: ((Boolean) -> Unit)?) {
        onFinish?.invoke(false)
    }

}