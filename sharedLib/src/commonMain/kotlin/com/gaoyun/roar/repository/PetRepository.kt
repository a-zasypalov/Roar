package com.gaoyun.roar.repository

import com.gaoyun.roar.domain.SynchronisationScheduler
import com.gaoyun.roar.model.domain.LanguageCode
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.toDomain
import com.gaoyun.roar.model.entity.RoarDatabase
import com.gaoyun.roar.network.PetsApi
import com.gaoyun.roar.network.SynchronisationApi
import com.gaoyun.roar.util.DatetimeConstants
import com.gaoyun.roar.util.Preferences
import com.gaoyun.roar.util.PreferencesKeys
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface PetRepository {
    fun getPet(id: String): Pet?
    suspend fun getPetsByUser(userId: String): List<Pet>
    fun insertPet(pet: Pet)
    fun deletePet(id: String)
    suspend fun getBreeds(petType: String, languageCode: LanguageCode): List<String>
}

class PetRepositoryImpl : PetRepository, KoinComponent {
    private val appDb: RoarDatabase by inject()
    private val api: PetsApi by inject()
    private val preferences: Preferences by inject()
    private val syncApi: SynchronisationApi by inject()
    private val scheduler: SynchronisationScheduler by inject()

    override fun getPet(id: String): Pet? {
        return appDb.petEntityQueries.selectById(id).executeAsOneOrNull()?.toDomain()
    }

    override suspend fun getPetsByUser(userId: String): List<Pet> {
        var cached = getCachedPetsByUser(userId)
        if (cached.isEmpty()) {
            syncApi.retrieveBackup {
                if (it) cached = getCachedPetsByUser(userId)
            }
        }

        return cached
    }

    private fun getCachedPetsByUser(userId: String): List<Pet> {
        return appDb.petEntityQueries.selectByUserId(userId).executeAsList().map { it.toDomain() }
    }

    override suspend fun getBreeds(petType: String, languageCode: LanguageCode): List<String> {
        val cachedBreeds = appDb.petBreedEntityQueries.selectByPetType(petType).executeAsList()
        val breedsLastUpdatedDateTime = preferences.getLong(PreferencesKeys.PET_BREEDS_LAST_UPDATE, 0L)
        val petBreedsLocale = preferences.getString(PreferencesKeys.PET_BREEDS_LOCALE, LanguageCode.English.name)
        return if (cachedBreeds.isEmpty()
            || languageCode != LanguageCode.valueOf(petBreedsLocale)
            || breedsLastUpdatedDateTime < Clock.System.now().toEpochMilliseconds() - DatetimeConstants.DAY_MILLIS
        ) {
            kotlin.runCatching {
                api.getPetBreedsByPetType(petType).let {
                    when (languageCode) {
                        LanguageCode.English -> it.breedsEn
                        LanguageCode.German -> it.breedsDe
                        LanguageCode.Russian -> it.breedsRu
                    }
                }
                    .also { appDb.petBreedEntityQueries.deleteAll() }
                    .onEach { appDb.petBreedEntityQueries.insertOrReplace(petType, it) }
                    .also {
                        preferences.setLong(PreferencesKeys.PET_BREEDS_LAST_UPDATE, Clock.System.now().toEpochMilliseconds())
                        preferences.setString(PreferencesKeys.PET_BREEDS_LOCALE, languageCode.name)
                    }
            }.getOrElse {
                it.printStackTrace()
                cachedBreeds.map { item -> item.breed }
            }
        } else {
            cachedBreeds.map { it.breed }
        }
    }

    override fun insertPet(pet: Pet) {
        appDb.petEntityQueries.insertOrReplace(
            id = pet.id,
            name = pet.name,
            breed = pet.breed,
            avatar = pet.avatar,
            userId = pet.userId,
            petType = pet.petType.toString(),
            birthday = pet.birthday.toString(),
            isSterilized = pet.isSterilized,
            gender = pet.gender.toString(),
            chipNumber = pet.chipNumber,
            dateCreated = pet.dateCreated.toString()
        )
        scheduler.scheduleSynchronisation()
    }

    override fun deletePet(id: String) {
        appDb.petEntityQueries.deleteById(id)
        scheduler.scheduleSynchronisation()
    }
}