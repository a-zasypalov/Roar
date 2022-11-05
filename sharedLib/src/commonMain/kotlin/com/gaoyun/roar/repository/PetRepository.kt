package com.gaoyun.roar.repository

import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.toDomain
import com.gaoyun.roar.model.entity.RoarDatabase
import com.gaoyun.roar.network.PetsApi
import com.gaoyun.roar.util.DatetimeConstants
import com.gaoyun.roar.util.Preferences
import com.gaoyun.roar.util.PreferencesKeys
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface PetRepository {
    fun getPet(id: String): Pet?
    fun getPetsByUser(userId: String): List<Pet>
    fun insertPet(pet: Pet)
    fun deletePet(id: String)
    suspend fun getBreeds(petType: String): List<String>
}

class PetRepositoryImpl : PetRepository, KoinComponent {
    private val appDb: RoarDatabase by inject()
    private val api: PetsApi by inject()
    private val preferences: Preferences by inject()

    override fun getPet(id: String): Pet? {
        return appDb.petEntityQueries.selectById(id).executeAsOneOrNull()?.toDomain()
    }

    override fun getPetsByUser(userId: String): List<Pet> {
        return appDb.petEntityQueries.selectByUserId(userId).executeAsList().map { it.toDomain() }
    }

    // TODO: Localisation
    override suspend fun getBreeds(petType: String): List<String> {
        val cachedBreeds = appDb.petBreedEntityQueries.selectByPetType(petType).executeAsList()
        val breedsLastUpdatedDateTime = preferences.getLong(PreferencesKeys.PET_BREEDS_LAST_UPDATE, 0L)

        return if (cachedBreeds.isEmpty() || (breedsLastUpdatedDateTime < Clock.System.now().toEpochMilliseconds() - DatetimeConstants.DAY_MILLIS)) {
            api.getPetBreedsByPetType(petType).breedsEn
                .onEach { appDb.petBreedEntityQueries.insertOrReplace(petType, it) }
        } else {
            cachedBreeds.map { it.breed }
        }
    }

    override fun insertPet(pet: Pet) {
        appDb.petEntityQueries.insertOrReplace(
            pet.id,
            pet.name,
            pet.breed,
            pet.userId,
            pet.avatar,
            pet.petType.toString(),
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