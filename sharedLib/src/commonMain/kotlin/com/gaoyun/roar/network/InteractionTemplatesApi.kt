package com.gaoyun.roar.network

import com.gaoyun.roar.model.dto.InteractionTemplatesListResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class InteractionTemplatesApi : KoinComponent {
    private val client: HttpClient by inject()

    internal suspend fun getInteractionTemplatesByPetType(petType: String): InteractionTemplatesListResponse {
        return client.requestAndCatch { get("${RoarApi.GITHUB_ENDPOINT}/app_config/${petType.lowercase()}_interaction_templates.json").body() }
    }

}