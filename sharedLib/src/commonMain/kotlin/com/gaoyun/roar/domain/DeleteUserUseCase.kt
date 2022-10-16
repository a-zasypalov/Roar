package com.gaoyun.roar.domain

import com.gaoyun.roar.repository.UserRepository
import com.gaoyun.roar.util.Preferences
import com.gaoyun.roar.util.PreferencesKeys
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DeleteUserUseCase : KoinComponent {

    private val repository: UserRepository by inject()
    private val prefs: Preferences by inject()

    suspend fun deleteCurrentUser() {
        prefs.getString(PreferencesKeys.CURRENT_USER_ID)?.let { userId ->
            repository.deleteUser(userId)
        }
    }

}