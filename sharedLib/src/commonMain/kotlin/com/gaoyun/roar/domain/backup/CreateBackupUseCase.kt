package com.gaoyun.roar.domain.backup

import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.domain.user.GetCurrentUserUseCase
import com.gaoyun.roar.model.domain.UserWithPets
import com.gaoyun.roar.model.domain.withInteractions
import com.gaoyun.roar.model.domain.withPets
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CreateBackupUseCase : KoinComponent {

    private val getCurrentUserUseCase: GetCurrentUserUseCase by inject()
    private val getPetUseCase: GetPetUseCase by inject()
    private val getInteraction: GetInteraction by inject()

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

        if(pets.isEmpty()) {
            emit(null)
        } else {
            val backupString = Json.encodeToString(UserWithPets.serializer(), user.withPets(pets))
            emit(backupString)
        }
    }

}