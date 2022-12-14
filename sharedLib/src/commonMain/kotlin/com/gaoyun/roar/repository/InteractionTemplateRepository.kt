package com.gaoyun.roar.repository

import com.gaoyun.roar.model.domain.interactions.InteractionTemplate
import com.gaoyun.roar.model.domain.interactions.toDomain
import com.gaoyun.roar.model.dto.toDomain
import com.gaoyun.roar.model.entity.RoarDatabase
import com.gaoyun.roar.network.InteractionTemplatesApi
import com.gaoyun.roar.util.DatetimeConstants.DAY_MILLIS
import com.gaoyun.roar.util.Preferences
import com.gaoyun.roar.util.PreferencesKeys.INTERACTION_TEMPLATES_LAST_UPDATE
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface InteractionTemplateRepository {
    suspend fun getInteractionTemplatesForPetType(type: String): List<InteractionTemplate>
    fun insertInteractionTemplate(interactionTemplate: InteractionTemplate)
    fun deleteAllTemplates()
}

class InteractionTemplateRepositoryImpl : InteractionTemplateRepository, KoinComponent {

    private val appDb: RoarDatabase by inject()
    private val api: InteractionTemplatesApi by inject()
    private val preferences: Preferences by inject()

    override suspend fun getInteractionTemplatesForPetType(type: String): List<InteractionTemplate> {
        val cachedTemplates = appDb.interactionTemplateEntityQueries.selectByPetType(type).executeAsList().map { it.toDomain() }
        val templatesLastUpdatedDateTime = preferences.getLong(INTERACTION_TEMPLATES_LAST_UPDATE, 0L)

        return if (cachedTemplates.isEmpty() || (templatesLastUpdatedDateTime < Clock.System.now().toEpochMilliseconds() - DAY_MILLIS)) {
            api.getInteractionTemplatesByPetType(type).items
                .map { template -> template.toDomain() }
                .onEach { template -> insertInteractionTemplate(template) }
                .also { preferences.setLong(INTERACTION_TEMPLATES_LAST_UPDATE, Clock.System.now().toEpochMilliseconds()) }
        } else {
            cachedTemplates
        }
    }

    override fun insertInteractionTemplate(interactionTemplate: InteractionTemplate) {
        appDb.interactionTemplateEntityQueries.insertOrReplace(
            id = interactionTemplate.id,
            petType = interactionTemplate.petType.toString(),
            type = interactionTemplate.type.toString(),
            name = interactionTemplate.name,
            interactionGroup = interactionTemplate.group.toString(),
            repeatConfig = interactionTemplate.repeatConfig.toString(),
        )
    }

    override fun deleteAllTemplates() {
        appDb.interactionTemplateEntityQueries.deleteAll()
    }

}