package com.gaoyun.roar.network

import org.koin.core.component.KoinComponent

expect class SynchronisationApi() : KoinComponent {
    fun sendBackup(backup: String)
    fun retrieveBackup()
}