package com.gaoyun.roar.network

import com.gaoyun.roar.model.dto.PetBreedsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.Json

class PetsApi(private val client: HttpClient) {

    internal suspend fun getPetBreedsByPetType(petType: String): PetBreedsResponse {
        val responseString: String = client.requestAndCatch {
            get("${RoarApi.GITHUB_ENDPOINT}/app_config/${petType.lowercase()}_breeds.json").body()
        }
        return Json.decodeFromString(responseString)
    }

}