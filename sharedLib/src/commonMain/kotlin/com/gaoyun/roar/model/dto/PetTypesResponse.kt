package com.gaoyun.roar.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PetTypesConfigResponse(
    @SerialName("pet_types")
    val types: List<PetTypeConfigResponse>
)

@Serializable
internal data class PetTypeConfigResponse(
    @SerialName("pet_type")
    val petType: String,
    @SerialName("breeds_location")
    val breedsLocation: String,
    @SerialName("interaction_templates_location")
    val interactionTemplatesLocation: String
)
