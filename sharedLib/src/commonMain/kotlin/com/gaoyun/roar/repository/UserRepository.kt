package com.gaoyun.roar.repository

import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.model.domain.toDomain
import com.gaoyun.roar.model.entity.RoarDatabase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface UserRepository {
    fun getUser(id: String): User?
    fun insertUser(user: User)
    fun deleteUsers()
}

class UserRepositoryImpl : UserRepository, KoinComponent {
    private val appDb: RoarDatabase by inject()

    override fun getUser(id: String): User? {
        return appDb.userEntityQueries.selectById(id).executeAsOneOrNull()?.toDomain()
    }

    override fun insertUser(user: User) {
        appDb.userEntityQueries.insert(user.id, user.name)
    }

    override fun deleteUsers() {
        appDb.userEntityQueries.removeAll()
    }
}