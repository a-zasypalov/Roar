package com.gaoyun.roar.domain.user

import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.repository.UserRepository
import com.gaoyun.roar.util.NoUserException
import com.gaoyun.roar.util.Preferences
import com.gaoyun.roar.util.PreferencesKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.cancellation.CancellationException

class GetCurrentUserUseCase(
    private val repository: UserRepository,
    private val prefs: Preferences,
) {

    @Throws(NoUserException::class, CancellationException::class)
    suspend fun getCurrentUser(): Flow<User> = flow {
        prefs.getString(PreferencesKeys.CURRENT_USER_ID)?.let { userId ->
            repository.getUser(userId)?.let {
                emit(it)
            } ?: throw NoUserException()
        }
    }

}