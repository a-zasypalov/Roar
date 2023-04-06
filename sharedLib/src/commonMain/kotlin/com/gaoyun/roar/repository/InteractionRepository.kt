package com.gaoyun.roar.repository

import com.gaoyun.roar.domain.SynchronisationScheduler
import com.gaoyun.roar.model.domain.interactions.Interaction
import com.gaoyun.roar.model.domain.interactions.toDomain
import com.gaoyun.roar.model.entity.RoarDatabase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface InteractionRepository {
    fun insertInteraction(interaction: Interaction)
    fun getInteraction(id: String): Interaction?
    fun getInteractionByPet(petId: String): List<Interaction>
    fun setInteractionIsActive(interactionId: String, isActive: Boolean)
    fun deleteInteraction(id: String)
    fun deletePetInteractions(petId: String)
}

class InteractionRepositoryImpl : InteractionRepository, KoinComponent {

    private val appDb: RoarDatabase by inject()
    private val scheduler: SynchronisationScheduler by inject()

    override fun getInteraction(id: String): Interaction? {
        return appDb.interactionEntityQueries.selectById(id).executeAsOneOrNull()?.toDomain()
    }

    override fun getInteractionByPet(petId: String): List<Interaction> {
        return appDb.interactionEntityQueries.selectByPetId(petId).executeAsList().map { it.toDomain() }
    }

    override fun insertInteraction(interaction: Interaction) {
        appDb.interactionEntityQueries.insertOrReplace(
            id = interaction.id,
            templateId = interaction.templateId,
            petId = interaction.petId,
            type = interaction.type.toString(),
            name = interaction.name,
            interactionGroup = interaction.group.toString(),
            repeatConfig = interaction.repeatConfig?.toString(),
            remindConfig = interaction.remindConfig.toString(),
            isActive = interaction.isActive,
            notes = interaction.notes
        )
        scheduler.scheduleSynchronisation()
    }

    override fun setInteractionIsActive(interactionId: String, isActive: Boolean) {
        val interaction = getInteraction(interactionId) ?: return
        insertInteraction(interaction.copy(isActive = isActive))
    }

    override fun deleteInteraction(id: String) {
        appDb.interactionEntityQueries.deleteById(id)
        scheduler.scheduleSynchronisation()
    }

    override fun deletePetInteractions(petId: String) {
        appDb.interactionEntityQueries.deleteByPetId(petId)
        scheduler.scheduleSynchronisation()
    }

}