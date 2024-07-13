package com.gaoyun.roar.domain.interaction

import com.gaoyun.roar.domain.AppPreferencesUseCase
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.PetWithInteractions
import com.gaoyun.roar.model.domain.interactions.InteractionGroup
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.model.domain.interactions.withReminders
import com.gaoyun.roar.model.domain.withInteractions
import com.gaoyun.roar.util.SharedDateUtils
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

class InteractionsListBuilder(
    private val getPetUseCase: GetPetUseCase,
    private val getInteractions: GetInteraction,
    private val appPreferencesUseCase: AppPreferencesUseCase,
) {

    suspend fun buildPetState(userId: String, screenModeFull: Boolean): List<PetWithInteractions> {
        val pets = getPetUseCase.getPetByUserId(userId).firstOrNull() ?: emptyList()

        return if (pets.size > 1 || screenModeFull.not()) {
            buildPetCardsList(pets)
        } else {
            pets.map { pet -> pet.withInteractions(buildInitialInteractionsListForPet(pet.id)) }
        }
    }

    private suspend fun buildPetCardsList(pets: List<Pet>) = pets.map { pet ->
        val interactions = getInteractions.getInteractionByPet(pet.id).firstOrNull() ?: emptyList()
        val reminders = interactions
            .flatMap { it.reminders }
            .filter { !it.isCompleted }
            .sortedBy { it.dateTime }
            .take(appPreferencesUseCase.numberOfRemindersOnMainScreen())

        val interactionsToShow = interactions.map { it.withReminders(reminders.filter { r -> r.interactionId == it.id }) }
            .filter { it.reminders.isNotEmpty() }
            .sortedBy { it.reminders.minOfOrNull { r -> r.dateTime } ?: SharedDateUtils.MAX_DATE }

        pet.withInteractions(interactionsToShow)
    }.sortedBy { pet ->
        pet.interactions.values.flatten()
            .minOfOrNull { interaction -> interaction.reminders.minOfOrNull { reminder -> reminder.dateTime } ?: SharedDateUtils.MAX_DATE }
            ?: SharedDateUtils.MIN_DATE
    }

    private suspend fun buildInitialInteractionsListForPet(petId: String) = buildInitialInteractionsListForPet(
        interactions = getInteractions.getInteractionByPet(petId).firstOrNull() ?: emptyList()
    )

    fun buildInitialInteractionsListForPet(interactions: List<InteractionWithReminders>) = interactions
        .filter { it.reminders.any { r -> !r.isCompleted } }
        .map {
            it.withReminders(it.reminders.toMutableList().filter { reminder -> !reminder.isCompleted })
        }
        .sortedBy { v ->
            v.reminders.filter { r -> !r.isCompleted }.minOfOrNull { r -> r.dateTime }
                ?: LocalDateTime(LocalDate.fromEpochDays(0), LocalTime(0, 0, 0))
        }
        .groupBy { it.group }

    suspend fun buildInactiveInteractionsListFor(petId: String) =
        buildInactiveInteractionsList(getInteractions.getInteractionByPet(petId).firstOrNull() ?: emptyList())

    fun buildInactiveInteractionsList(interactions: List<InteractionWithReminders>) = interactions
        .filter { it.reminders.all { r -> r.isCompleted } }
        .sortedByDescending { v ->
            v.reminders.maxOfOrNull { r -> r.dateTime }
                ?: LocalDateTime(LocalDate.fromEpochDays(0), LocalTime(0, 0, 0))
        }

    fun buildListOnCompletingReminder(
        originalList: Map<InteractionGroup, List<InteractionWithReminders>>,
        completedInteraction: InteractionWithReminders,
        isComplete: Boolean,
    ): Map<InteractionGroup, List<InteractionWithReminders>> {
        val newInteractions = originalList.toMutableMap()

        val newList = originalList[completedInteraction.group]?.toMutableList()?.apply {
            indexOfFirst { item -> item.id == completedInteraction.id }.takeIf { it > -1 }?.let { index ->
                val interactionToShow =
                    if (isComplete) completedInteraction else completedInteraction.withReminders(completedInteraction.reminders.filter { !it.isCompleted })
                set(index, interactionToShow)
            }
        }

        newInteractions[completedInteraction.group] = newList ?: emptyList()

        return newInteractions
    }

}