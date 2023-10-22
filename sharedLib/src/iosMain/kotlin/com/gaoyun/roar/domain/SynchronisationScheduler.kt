package com.gaoyun.roar.domain

import com.gaoyun.roar.domain.backup.CreateBackupUseCase
import com.gaoyun.roar.model.domain.NotificationData
import com.gaoyun.roar.network.SynchronisationApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SynchronisationSchedulerImpl : SynchronisationScheduler, KoinComponent {
    val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val createBackupUseCase: CreateBackupUseCase by inject()
    private val api: SynchronisationApi by inject()

    private var job: Job? = null

    override fun scheduleSynchronisation() {
        if(job == null) {
            job = scope.launch {
                createBackupUseCase.createBackupToSync()
                    .catch { it.printStackTrace() }
                    .map {
                        try {
                            if (it != null) api.sendBackup(it)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    .first()
                job = null
            }
        }
    }
}

class NotificationSchedulerImpl : NotificationScheduler {
    override fun cancelNotification(id: String?) {

    }

    override fun scheduleNotification(data: NotificationData) {

    }
}