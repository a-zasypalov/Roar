package com.gaoyun.roar.domain.user

import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.repository.UserRepository
import com.gaoyun.roar.util.Preferences
import com.gaoyun.roar.util.PreferencesKeys
import com.gaoyun.roar.util.randomUUID
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RegisterUserUseCase : KoinComponent {

    private val repository: UserRepository by inject()
    private val prefs: Preferences by inject()

    suspend fun register(name: String) {
        val newUser = User(
            id = randomUUID(),
            name = name
        )

        repository.apply {
            deleteUsers()
            insertUser(newUser)
        }
        prefs.setString(PreferencesKeys.CURRENT_USER_ID, newUser.id)
    }

    suspend fun registerFromBackup(user: User) {
        repository.apply {
            deleteUsers()
            insertUser(user)
        }
        prefs.setString(PreferencesKeys.CURRENT_USER_ID, user.id)
    }

}