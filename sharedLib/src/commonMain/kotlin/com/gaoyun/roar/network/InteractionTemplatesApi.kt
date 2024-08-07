package com.gaoyun.roar.network

import com.gaoyun.roar.model.dto.InteractionTemplatesListResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.Json

class InteractionTemplatesApi(private val client: HttpClient) {

    internal suspend fun getInteractionTemplatesByPetType(petType: String): InteractionTemplatesListResponse {
        val responseString: String = client.requestAndCatch {
            get("${RoarApi.GITHUB_ENDPOINT}/app_config/${petType.lowercase()}_interaction_templates.json").body()
        }
        return Json.decodeFromString(responseString)
    }

}