package com.gaoyun.roar.domain.backup

import com.gaoyun.roar.domain.interaction.InsertInteraction
import com.gaoyun.roar.domain.interaction.RemoveInteraction
import com.gaoyun.roar.domain.pet.AddPetUseCase
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.domain.pet.RemovePetUseCase
import com.gaoyun.roar.domain.reminder.InsertReminder
import com.gaoyun.roar.domain.user.GetCurrentUserUseCase
import com.gaoyun.roar.model.domain.UserWithPets
import com.gaoyun.roar.model.domain.interactions.withoutReminders
import com.gaoyun.roar.model.domain.withoutInteractions
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ImportBackupUseCase : KoinComponent {

    private val addPetUseCase: AddPetUseCase by inject()
    private val insertInteraction: InsertInteraction by inject()
    private val insertReminder: InsertReminder by inject()

    private val getCurrentUserUseCase: GetCurrentUserUseCase by inject()
    private val getPetUseCase: GetPetUseCase by inject()

    private val removePetUseCase: RemovePetUseCase by inject()
    private val removeInteraction: RemoveInteraction by inject()

    fun importBackup(backup: ByteArray, removeOld: Boolean) = flow {
        try {
            val user = Json.decodeFromString(UserWithPets.serializer(), backup.decodeToString())
            val currentUserId = getCurrentUserUseCase.getCurrentUser().firstOrNull()?.id ?: ""

            if (removeOld) {
                val petIds = getPetUseCase.getPetByUserId(currentUserId).firstOrNull() ?: listOf()

                petIds.forEach {
                    removeInteraction.removeInteractionByPet(it.id).firstOrNull()
                    removePetUseCase.removePet(it.id).firstOrNull()
                }
            }

            user.pets.map { pet ->
                addPetUseCase.addPet(pet.withoutInteractions().copy(userId = currentUserId)).firstOrNull()
                return@map pet
            }.flatMap { pet ->
                pet.interactions.values.flatten()
            }.flatMap { interaction ->
                insertInteraction.insertInteraction(interaction.withoutReminders()).firstOrNull()
                return@flatMap interaction.reminders
            }.forEach { reminder ->
                insertReminder.insertReminder(reminder).firstOrNull()
            }

            emit(true)
        } catch (e: SerializationException) {
            e.printStackTrace()
            emit(false)
        }
    }

}