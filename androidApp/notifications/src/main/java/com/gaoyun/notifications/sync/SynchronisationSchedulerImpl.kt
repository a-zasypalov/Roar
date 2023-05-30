package com.gaoyun.notifications.sync

import android.content.Context
import androidx.work.*
import com.gaoyun.roar.domain.SynchronisationScheduler
import com.gaoyun.roar.domain.backup.CreateBackupUseCase
import com.gaoyun.roar.network.SynchronisationApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

class SynchronisationSchedulerImpl(
    private val workManager: WorkManager,
) : KoinComponent, SynchronisationScheduler {

    override fun scheduleSynchronisation() {
        val request = OneTimeWorkRequestBuilder<SynchronisationWorker>()
            .setInitialDelay(30, TimeUnit.SECONDS)
            .build()

        workManager.enqueueUniqueWork("sync", ExistingWorkPolicy.REPLACE, request)
    }

}

class SynchronisationWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params), KoinComponent {

    private val api: SynchronisationApi by inject()
    private val createBackupUseCase: CreateBackupUseCase by inject()

    override suspend fun doWork(): Result {
        return createBackupUseCase.createBackupToSync()
            .catch { Result.failure() }
            .map {
                try {
                    if (it != null) api.sendBackup(it)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                Result.success()
            }
            .first()
    }
}