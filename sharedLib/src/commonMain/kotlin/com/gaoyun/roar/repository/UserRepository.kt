package com.gaoyun.roar.repository

import com.gaoyun.roar.domain.SynchronisationScheduler
import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.model.domain.toDomain
import com.gaoyun.roar.model.entity.RoarDatabase

interface UserRepository {
    suspend fun getUser(id: String): User?
    suspend fun insertUser(user: User)
    suspend fun deleteUsers()
}

class UserRepositoryImpl(
    private val appDb: RoarDatabase,
    private val scheduler: SynchronisationScheduler,
) : UserRepository {

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