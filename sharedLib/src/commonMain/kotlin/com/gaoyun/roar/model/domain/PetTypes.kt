package com.gaoyun.roar.model.domain

import com.gaoyun.roar.model.dto.PetTypesResponse

internal data class PetType(
    val petType: String,
    val breedsLocation: String,
    val interactionTemplatesLocation: String
)

internal fun PetTypesResponse.toDomain(): List<PetType> = types.map {
    PetType(
        petType = it.petType,
        breedsLocation = it.breedsLocation,
        interactionTemplatesLocation = it.interactionTemplatesLocation
    )
}