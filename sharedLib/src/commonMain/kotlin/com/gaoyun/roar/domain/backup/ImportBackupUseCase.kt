package com.gaoyun.roar.domain.backup

import com.gaoyun.roar.domain.interaction.InsertInteraction
import com.gaoyun.roar.domain.pet.AddPetUseCase
import com.gaoyun.roar.domain.reminder.InsertReminder
import com.gaoyun.roar.domain.user.RegisterUserUseCase
import com.gaoyun.roar.model.domain.UserWithPets
import com.gaoyun.roar.model.domain.interactions.withoutReminders
import com.gaoyun.roar.model.domain.withoutInteractions
import com.gaoyun.roar.model.domain.withoutPets
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ImportBackupUseCase : KoinComponent {

    private val registerUserUseCase: RegisterUserUseCase by inject()
    private val addPetUseCase: AddPetUseCase by inject()
    private val insertInteraction: InsertInteraction by inject()
    private val insertReminder: InsertReminder by inject()

    fun importBackup(backupString: String) = flow {
        try {
            val user = Json.decodeFromString(UserWithPets.serializer(), backupString)
            registerUserUseCase.registerFromBackup(user.withoutPets())

            user.pets.map { pet ->
                addPetUseCase.addPet(pet.withoutInteractions()).firstOrNull()
                return@map pet
            }.flatMap { pet ->
                pet.interactions
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