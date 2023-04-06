package com.gaoyun.roar.repository

import com.gaoyun.roar.domain.SynchronisationScheduler
import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.model.domain.toDomain
import com.gaoyun.roar.model.entity.RoarDatabase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface UserRepository {
    suspend fun getUser(id: String): User?
    suspend fun insertUser(user: User)
    suspend fun deleteUsers()
}

class UserRepositoryImpl : UserRepository, KoinComponent {
    private val appDb: RoarDatabase by inject()
    private val scheduler: SynchronisationScheduler by inject()

    override suspend fun getUser(id: String): User? {
        return appDb.userEntityQueries.selectById(id).executeAsOneOrNull()?.toDomain()
    }

    override suspend fun insertUser(user: User) {
        appDb.userEntityQueries.insert(user.id, user.name)
        scheduler.scheduleSynchronisation()
    }

    override suspend fun deleteUsers() {
        appDb.userEntityQueries.removeAll()
    }
}