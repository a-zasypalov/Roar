package com.gaoyun.roar.util

import com.gaoyun.roar.presentation.user_screen.UserScreenContract

interface BackupHandler {
    fun registerExecutor()
    fun unregisterExecutor()
    fun exportBackup(backup: String, callback: () -> Unit)
    fun importBackup(onUseBackup: (UserScreenContract.Event.OnUseBackup) -> Unit)
}

class NoopBackupHandler : BackupHandler {
    override fun exportBackup(backup: String, callback: () -> Unit) {}
    override fun registerExecutor() {}
    override fun unregisterExecutor() {}
    override fun importBackup(onUseBackup: (UserScreenContract.Event.OnUseBackup) -> Unit) {}
}