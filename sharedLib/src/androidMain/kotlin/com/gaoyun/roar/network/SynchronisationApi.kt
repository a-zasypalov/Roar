package com.gaoyun.roar.network

import com.gaoyun.roar.domain.sync.SynchronisationUseCase
import com.gaoyun.roar.util.Preferences
import com.gaoyun.roar.util.PreferencesKeys
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SynchronisationApiAndroid : KoinComponent, SynchronisationApi {

    private val storageRef = Firebase.storage.reference
    private val prefs: Preferences by inject()
    private val scope = MainScope()
    private val synchronisationUseCase: SynchronisationUseCase by inject()

    override fun sendBackup(backup: String) {
        prefs.getString(PreferencesKeys.CURRENT_USER_ID)?.let { userId ->
            storageRef.child("sync_data/$userId.json")
                .putBytes(backup.encodeToByteArray())
                .addOnSuccessListener { println("Sync succeed!") }
                .addOnFailureListener { println("Sync failed!\n$it") }
        }
    }

    override suspend fun retrieveBackup(onFinish: ((Boolean) -> Unit), onAuthException: () -> Unit) {
        prefs.getString(PreferencesKeys.CURRENT_USER_ID)?.let { userId ->
            storageRef.child("sync_data/$userId.json")
                .getBytes(Long.MAX_VALUE)
                .addOnSuccessListener {
                    scope.launch {
                        println("Synced")
                        synchronisationUseCase.sync(it).watch {
                            onFinish.invoke(it)
                        }
                    }
                }
                .addOnFailureListener {
                    println("Sync failed!\n$it")
                    onFinish.invoke(false)
                    Firebase.auth.currentUser?.getIdToken(true)?.addOnFailureListener { e -> e.printStackTrace(); onAuthException() }
                }
        }
    }

}