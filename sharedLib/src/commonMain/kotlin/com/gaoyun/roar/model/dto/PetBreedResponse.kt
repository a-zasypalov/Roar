package com.gaoyun.roar.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PetBreedsResponse(
    @SerialName("breeds_en")
    val breedsEn: List<String>,
    @SerialName("breeds_de")
    val breedsDe: List<String>,
    @SerialName("breeds_ru")
    val breedsRu: List<String>
)