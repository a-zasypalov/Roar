package com.gaoyun.roar.network

import com.gaoyun.roar.model.dto.PetBreedsResponse
import io.ktor.client.*
import io.ktor.client.request.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PetsApi : KoinComponent {
    private val client: HttpClient by inject()

    internal suspend fun getPetBreedsByPetType(petType: String): PetBreedsResponse {
        return client.requestAndCatch { get("${RoarApi.V1_ENDPOINT}/app_config/${petType.lowercase()}_breeds.json") }
    }

}