package com.gaoyun.roar.repository

import com.gaoyun.roar.model.domain.InteractionTemplate
import com.gaoyun.roar.model.domain.encodeAdditionalFields
import com.gaoyun.roar.model.domain.toDomain
import com.gaoyun.roar.model.entity.RoarDatabase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface InteractionTemplateRepository {
    fun getInteractionTemplatesForPetType(type: String): List<InteractionTemplate>
    fun insertInteractionTemplate(interactionTemplate: InteractionTemplate)
    fun deleteAllTemplates()
}

class InteractionTemplateRepositoryImpl : InteractionTemplateRepository, KoinComponent {

    private val appDb: RoarDatabase by inject()

    override fun getInteractionTemplatesForPetType(type: String): List<InteractionTemplate> {
        return appDb.interactionTemplateEntityQueries.selectByPetType(type).executeAsList().map { it.toDomain() }
    }

    override fun insertInteractionTemplate(interactionTemplate: InteractionTemplate) {
        appDb.interactionTemplateEntityQueries.insertOrReplace(
            id = interactionTemplate.id,
            petType = interactionTemplate.petType.toString(),
            type = interactionTemplate.type.toString(),
            name = interactionTemplate.name,
            interactionGroup = interactionTemplate.group.toString(),
            repeatConfig = interactionTemplate.repeatConfig.toString(),
            additionalFields = interactionTemplate.additionalFields.encodeAdditionalFields()
        )
    }

    override fun deleteAllTemplates() {
        appDb.interactionTemplateEntityQueries.deleteAll()
    }

}