package com.gaoyun.roar.domain.user

import com.gaoyun.roar.util.Preferences
import com.gaoyun.roar.util.PreferencesKeys
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.storage.storage

class DeleteRemoteAccountExecutorImpl(private val preferences: Preferences) : DeleteRemoteAccountExecutor {
    override fun deleteAccount() {
        try {
            preferences.getString(PreferencesKeys.CURRENT_USER_ID)?.let {
                Firebase.storage.reference.child("sync_data/$it.json").delete()
            }
            Firebase.auth.currentUser?.delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}