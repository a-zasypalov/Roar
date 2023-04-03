package com.gaoyun.roar.network

import com.gaoyun.roar.domain.backup.ImportBackupUseCase
import com.gaoyun.roar.util.Preferences
import com.gaoyun.roar.util.PreferencesKeys
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class SynchronisationApi : KoinComponent {

    private val storageRef = Firebase.storage.reference
    private val prefs: Preferences by inject()
    private val importBackupUseCase: ImportBackupUseCase by inject()

    actual fun sendBackup(backup: String) {
        prefs.getString(PreferencesKeys.CURRENT_USER_ID)?.let { userId ->
            storageRef.child("sync_data/$userId.json")
                .putBytes(backup.encodeToByteArray())
                .addOnSuccessListener { println("Sync succeed!") }
                .addOnFailureListener { println("Sync failed!\n$it") }
        }
    }

    actual fun retrieveBackup() {
        prefs.getString(PreferencesKeys.CURRENT_USER_ID)?.let { userId ->
            storageRef.child("sync_data/$userId.json")
                .getBytes(Long.MAX_VALUE)
                .addOnSuccessListener {
                    println(it.decodeToString())
                    //importBackupUseCase.importBackup(it.contentToString(), removeOld = false)
                }
                .addOnFailureListener { println("Sync failed!\n$it") }
        }
    }

}