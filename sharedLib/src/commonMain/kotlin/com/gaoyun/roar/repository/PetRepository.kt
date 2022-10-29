package com.gaoyun.roar.repository

import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.toDomain
import com.gaoyun.roar.model.entity.RoarDatabase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface PetRepository {
    suspend fun getPet(id: String): Pet?
    suspend fun getPetsByUser(userId: String): List<Pet>
    suspend fun insertPet(pet: Pet)
    suspend fun deletePet(id: String)
}

class PetRepositoryImpl : PetRepository, KoinComponent {
    private val appDb: RoarDatabase by inject()

    override suspend fun getPet(id: String): Pet? {
        return appDb.petEntityQueries.selectById(id).executeAsOneOrNull()?.toDomain()
    }

    override suspend fun getPetsByUser(userId: String): List<Pet> {
        return appDb.petEntityQueries.selectByUserId(userId).executeAsList().map { it.toDomain() }
    }

    override suspend fun insertPet(pet: Pet) {
        appDb.petEntityQueries.insertOrReplace(
            pet.id,
            pet.name,
            pet.petType,
            pet.breed,
            pet.userId,
            pet.birthday.toString(),
            pet.isSterilized,
            pet.dateCreated.toString(),
            pet.reminders
        )
    }

    override suspend fun deletePet(id: String) {
        appDb.petEntityQueries.deleteById(id)
    }
}