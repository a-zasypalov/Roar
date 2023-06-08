package com.gaoyun.roar.domain.user

import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.repository.UserRepository
import kotlinx.coroutines.flow.flow

class EditUserUseCase(private val repository: UserRepository) {

    suspend fun update(user: User) = flow {
        repository.insertUser(user)
        emit(user)
    }
}