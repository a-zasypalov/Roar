package com.gaoyun.roar.domain.backup

import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.domain.user.GetCurrentUserUseCase
import com.gaoyun.roar.model.domain.UserWithPets
import com.gaoyun.roar.model.domain.withInteractions
import com.gaoyun.roar.model.domain.withPets
import com.gaoyun.roar.util.Preferences
import com.gaoyun.roar.util.PreferencesKeys
import com.gaoyun.roar.util.PreferencesKeys.LAST_SYNCHRONISED_HASH
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
class CreateBackupUseCase(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getPetUseCase: GetPetUseCase,
    private val getInteraction: GetInteraction,
    private val prefs: Preferences,
) {

    fun createBackup() = flow {
        val user = getCurrentUserUseCase.getCurrentUser().firstOrNull() ?: return@flow

        val pets = getPetUseCase.getPetByUserIdForBackup(user.id).firstOrNull()
            ?.map {
                val interactions = getInteraction.getInteractionByPet(it.id).firstOrNull() ?: listOf()
                it.withInteractions(interactions.groupBy { i -> i.group })
            }
            ?: listOf()

        val backupString = Json.encodeToString(UserWithPets.serializer(), user.withPets(pets))

        emit(backupString)
    }

    fun createBackupToSync() = flow {
        val user = getCurrentUserUseCase.getCurrentUser().firstOrNull() ?: return@flow

        val pets = getPetUseCase.getPetByUserIdForBackup(user.id).firstOrNull()
            ?.map {
                val interactions = getInteraction.getInteractionByPet(it.id).firstOrNull() ?: listOf()
                it.withInteractions(interactions.groupBy { i -> i.group })
            }
            ?: listOf()

        if (pets.isEmpty()) {
            emit(null)
        } else {
            val userWithPets = user.withPets(pets)
            prefs.setLong(PreferencesKeys.LAST_SYNCHRONISED_TIMESTAMP, userWithPets.timestamp)
            val backupString = Json.encodeToString(UserWithPets.serializer(), userWithPets)

            val hash = Base64.encode(backupString.encodeToByteArray())
            if (hash != prefs.getString(LAST_SYNCHRONISED_HASH)) {
                prefs.setString(LAST_SYNCHRONISED_HASH, hash)
                emit(backupString)
            } else {
                emit(null)
            }
        }
    }

}