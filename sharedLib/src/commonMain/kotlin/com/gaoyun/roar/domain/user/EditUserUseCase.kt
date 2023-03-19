package com.gaoyun.roar.domain.user

import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.repository.UserRepository
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EditUserUseCase : KoinComponent {

    private val repository: UserRepository by inject()

    suspend fun update(user: User) = flow {
        repository.insertUser(user)
        emit(user)
    }
}