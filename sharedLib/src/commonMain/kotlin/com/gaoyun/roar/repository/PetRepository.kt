package com.gaoyun.roar.repository

import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.toDomain
import com.gaoyun.roar.model.entity.RoarDatabase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface PetRepository {
    fun getPet(id: String): Pet?
    fun getPetsByUser(userId: String): List<Pet>
    fun insertPet(pet: Pet)
    fun deletePet(id: String)
}

class PetRepositoryImpl : PetRepository, KoinComponent {
    private val appDb: RoarDatabase by inject()

    override fun getPet(id: String): Pet? {
        return appDb.petEntityQueries.selectById(id).executeAsOneOrNull()?.toDomain()
    }

    override fun getPetsByUser(userId: String): List<Pet> {
        return appDb.petEntityQueries.selectByUserId(userId).executeAsList().map { it.toDomain() }
    }

    override fun insertPet(pet: Pet) {
        appDb.petEntityQueries.insertOrReplace(
            pet.id,
            pet.name,
            pet.petType.toString(),
            pet.breed,
            pet.userId,
            pet.birthday.toString(),
            pet.isSterilized,
            pet.dateCreated.toString(),
            pet.reminders
        )
    }

    override fun deletePet(id: String) {
        appDb.petEntityQueries.deleteById(id)
    }
}