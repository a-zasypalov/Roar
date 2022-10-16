package com.gaoyun.roar.domain

import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.repository.UserRepository
import com.gaoyun.roar.util.NoUserException
import com.gaoyun.roar.util.Preferences
import com.gaoyun.roar.util.PreferencesKeys
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.cancellation.CancellationException

class GetCurrentUserUseCase: KoinComponent {

    private val repository: UserRepository by inject()
    private val prefs: Preferences by inject()

    @Throws(NoUserException::class, CancellationException::class)
    suspend fun getCurrentUser(): User {
        prefs.getString(PreferencesKeys.CURRENT_USER_ID)?.let { userId ->
            return repository.getUser(userId) ?: throw NoUserException()
        }

        throw NoUserException()
    }

}