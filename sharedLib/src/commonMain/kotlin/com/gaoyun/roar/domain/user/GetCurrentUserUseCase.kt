package com.gaoyun.roar.domain.user

import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.repository.UserRepository
import com.gaoyun.roar.util.Preferences
import com.gaoyun.roar.util.PreferencesKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetCurrentUserUseCase(
    private val repository: UserRepository,
    private val prefs: Preferences,
) {

    suspend fun getCurrentUser(): Flow<User?> = flow {
        prefs.getString(PreferencesKeys.CURRENT_USER_ID)?.let { userId ->
            emit(repository.getUser(userId))
        }
    }

}