package com.gaoyun.roar.network

interface SynchronisationApi {
    fun sendBackup(backup: String)
    suspend fun retrieveBackup(onFinish: ((Boolean) -> Unit)? = null)
}