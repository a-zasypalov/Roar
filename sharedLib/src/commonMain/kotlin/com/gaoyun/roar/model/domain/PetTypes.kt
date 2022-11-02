package com.gaoyun.roar.model.domain

import com.gaoyun.roar.model.dto.PetTypesConfigResponse

internal data class PetTypeConfig(
    val petType: String,
    val breedsLocation: String,
    val interactionTemplatesLocation: String
)

internal fun PetTypesConfigResponse.toDomain(): List<PetTypeConfig> = types.map {
    PetTypeConfig(
        petType = it.petType,
        breedsLocation = it.breedsLocation,
        interactionTemplatesLocation = it.interactionTemplatesLocation
    )
}