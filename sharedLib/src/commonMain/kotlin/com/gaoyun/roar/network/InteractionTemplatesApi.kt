package com.gaoyun.roar.network

import com.gaoyun.roar.model.dto.InteractionTemplatesListResponse
import io.ktor.client.*
import io.ktor.client.request.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class InteractionTemplatesApi : KoinComponent {
    private val client: HttpClient by inject()

    internal suspend fun getInteractionTemplatesByPetType(petType: String): InteractionTemplatesListResponse {
        return client.requestAndCatch { get("${RoarApi.V1_ENDPOINT}/app_config/${petType.lowercase()}_interaction_templates.json") }
    }

}