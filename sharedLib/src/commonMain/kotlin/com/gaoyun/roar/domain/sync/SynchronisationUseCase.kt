package com.gaoyun.roar.domain.sync

import com.gaoyun.roar.domain.interaction.InsertInteraction
import com.gaoyun.roar.domain.interaction.RemoveInteraction
import com.gaoyun.roar.domain.pet.AddPetUseCase
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.domain.pet.RemovePetUseCase
import com.gaoyun.roar.domain.reminder.InsertReminder
import com.gaoyun.roar.domain.user.EditUserUseCase
import com.gaoyun.roar.domain.user.GetCurrentUserUseCase
import com.gaoyun.roar.model.domain.UserWithPets
import com.gaoyun.roar.model.domain.interactions.withoutReminders
import com.gaoyun.roar.model.domain.withoutInteractions
import com.gaoyun.roar.model.domain.withoutPets
import com.gaoyun.roar.util.Preferences
import com.gaoyun.roar.util.PreferencesKeys
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SynchronisationUseCase : KoinComponent {

    private val addPetUseCase: AddPetUseCase by inject()
    private val insertInteraction: InsertInteraction by inject()
    private val insertReminder: InsertReminder by inject()

    private val getCurrentUserUseCase: GetCurrentUserUseCase by inject()
    private val getPetUseCase: GetPetUseCase by inject()

    private val removePetUseCase: RemovePetUseCase by inject()
    private val removeInteraction: RemoveInteraction by inject()

    private val editUserUseCase: EditUserUseCase by inject()

    private val prefs: Preferences by inject()

    fun sync(backup: ByteArray) = flow {
        try {
            val user = Json.decodeFromString(UserWithPets.serializer(), backup.decodeToString())
            val timestamp = prefs.getLong(PreferencesKeys.LAST_SYNCHRONISED_TIMESTAMP, 0L)

            if(timestamp < user.timestamp) {
                prefs.setLong(PreferencesKeys.LAST_SYNCHRONISED_TIMESTAMP, user.timestamp)
                println("Apply synced data")

                val currentUserId = getCurrentUserUseCase.getCurrentUser().firstOrNull()?.id ?: ""

                getPetUseCase.getPetByUserId(currentUserId)
                    .firstOrNull()
                    ?.forEach {
                        removeInteraction.removeInteractionByPetToSync(it.id).firstOrNull()
                        removePetUseCase.removePet(it.id).firstOrNull()
                    }

                editUserUseCase.update(user.withoutPets())

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
            }

            emit(true)
        } catch (e: SerializationException) {
            e.printStackTrace()
            emit(false)
        }
    }

}