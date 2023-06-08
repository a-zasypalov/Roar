package com.gaoyun.roar.domain.user

import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.repository.UserRepository
import com.gaoyun.roar.util.Preferences
import com.gaoyun.roar.util.PreferencesKeys

class RegisterUserUseCase(
    private val repository: UserRepository,
    private val prefs: Preferences,
) {

    suspend fun register(name: String, id: String) {
        val newUser = User(
            id = id,
            name = name.trim()
        )

        repository.apply {
            deleteUsers()
            insertUser(newUser)
        }
        prefs.setString(PreferencesKeys.CURRENT_USER_ID, newUser.id)
    }

    fun login(id: String) {
        prefs.setString(PreferencesKeys.CURRENT_USER_ID, id)
    }

}