package com.gaoyun.roar.network

import com.gaoyun.roar.model.dto.PetBreedsResponse
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PetsApi : KoinComponent {
    private val client: HttpClient by inject()

    internal suspend fun getPetBreedsByPetType(petType: String): PetBreedsResponse {
        return client.requestAndCatch { get("${RoarApi.GITHUB_ENDPOINT}/app_config/${petType.lowercase()}_breeds.json").body() }
    }

}