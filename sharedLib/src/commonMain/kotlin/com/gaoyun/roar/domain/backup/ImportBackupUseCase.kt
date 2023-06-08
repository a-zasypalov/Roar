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

class ImportBackupUseCase(
    private val addPetUseCase: AddPetUseCase,
    private val insertInteraction: InsertInteraction,
    private val insertReminder: InsertReminder,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getPetUseCase: GetPetUseCase,
    private val removePetUseCase: RemovePetUseCase,
    private val removeInteraction: RemoveInteraction,
) {

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