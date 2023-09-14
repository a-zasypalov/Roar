package com.gaoyun.roar.network

import com.gaoyun.roar.domain.sync.SynchronisationUseCase
import com.gaoyun.roar.util.Preferences
import com.gaoyun.roar.util.PreferencesKeys
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.storage.storage
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.utils.io.core.toByteArray
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.dataUsingEncoding

actual class SynchronisationApi : KoinComponent {

    private val storageRef = Firebase.storage.reference
    private val prefs: Preferences by inject()
    private val synchronisationUseCase: SynchronisationUseCase by inject()
    private val client: HttpClient by inject()

    actual fun sendBackup(backup: String) {
        prefs.getString(PreferencesKeys.CURRENT_USER_ID)?.let { userId ->
            NSString.create(string = backup).dataUsingEncoding(NSUTF8StringEncoding)?.let { data ->
                storageRef.child("sync_data/$userId.json")
                    .ios
                    .putData(uploadData = data, metadata = null) { _, error ->
                        if (error != null) {
                            println("Sync failed!\n$error")
                        } else {
                            println("Sync succeed!")
                        }
                    }
            }
        }
    }

    actual suspend fun retrieveBackup(onFinish: ((Boolean) -> Unit)?) {
        var file = byteArrayOf()

        prefs.getString(PreferencesKeys.CURRENT_USER_ID)?.let { userId ->
            client.requestAndCatch(
                {
                    file = get(storageRef.child("sync_data/$userId.json").getDownloadUrl())
                        .bodyAsText().toByteArray()
                },
                { onFinish?.invoke(false) }
            )

            synchronisationUseCase.sync(file).collect {
                onFinish?.invoke(it)
            }
        }
    }

}