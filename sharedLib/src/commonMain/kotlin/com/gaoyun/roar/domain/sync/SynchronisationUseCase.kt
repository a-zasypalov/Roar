package com.gaoyun.roar.domain.sync

import com.gaoyun.roar.domain.NotificationScheduler
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
import com.gaoyun.roar.util.asCommonFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

class SynchronisationUseCase(
    private val addPetUseCase: AddPetUseCase,
    private val insertInteraction: InsertInteraction,
    private val insertReminder: InsertReminder,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getPetUseCase: GetPetUseCase,
    private val removePetUseCase: RemovePetUseCase,
    private val removeInteraction: RemoveInteraction,
    private val editUserUseCase: EditUserUseCase,
    private val notificationScheduler: NotificationScheduler,
    private val prefs: Preferences,
) {

    fun sync(backup: ByteArray) = flow {
        try {
            val user = Json.decodeFromString(UserWithPets.serializer(), backup.decodeToString())
            val timestamp = prefs.getLong(PreferencesKeys.LAST_SYNCHRONISED_TIMESTAMP, 0L)

            if (timestamp < user.timestamp) {
                prefs.setLong(PreferencesKeys.LAST_SYNCHRONISED_TIMESTAMP, user.timestamp)
                println("Apply synced data")

                val currentUserId = getCurrentUserUseCase.getCurrentUser().firstOrNull()?.id ?: ""

                getPetUseCase.getPetByUserId(currentUserId)
                    .firstOrNull()
                    ?.forEach {
                        removeInteraction.removeInteractionByPetToSync(it.id).firstOrNull()
                        removePetUseCase.removePet(it.id).firstOrNull()
                    }

                editUserUseCase.update(user.withoutPets()).firstOrNull()

                notificationScheduler.scheduledNotificationIds { scheduledJobIds ->
                    val syncedJobs = user.pets.flatMap { pet ->
                        pet.interactions.values.flatten().flatMap { interaction ->
                            interaction.reminders.mapNotNull {
                                it.notificationJobId
                            }
                        }
                    }

                    val jobsToCancel = scheduledJobIds.filter { !syncedJobs.contains(it) }
                    notificationScheduler.cancelNotifications(jobsToCancel)
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
                    insertReminder.insertReminderAndScheduleNotification(reminder).firstOrNull()
                }
            }

            emit(true)
        } catch (e: SerializationException) {
            e.printStackTrace()
            emit(false)
        }
    }.asCommonFlow()

}