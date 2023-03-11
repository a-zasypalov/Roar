package com.gaoyun.common

import com.gaoyun.roar.model.domain.PetType
import com.gaoyun.roar.model.domain.interactions.InteractionGroup
import com.gaoyun.roar.model.domain.interactions.InteractionType

fun InteractionType.icon() = when (this) {
    InteractionType.HEALTH_CHECK -> R.drawable.ic_health_check
    InteractionType.FLEES -> R.drawable.ic_drops
    InteractionType.DEWORMING -> R.drawable.ic_pills
    InteractionType.BATHING -> R.drawable.ic_soap
    InteractionType.VACCINATION -> R.drawable.ic_vaccine
    InteractionType.PILLS -> R.drawable.ic_pills
    InteractionType.GROOMING -> R.drawable.ic_grooming
    InteractionType.NAILS -> R.drawable.ic_nails
    InteractionType.CUSTOM -> R.drawable.ic_care
}

fun InteractionGroup.icon() = when (this) {
    InteractionGroup.HEALTH -> R.drawable.ic_medical
    InteractionGroup.CARE -> R.drawable.ic_care
    InteractionGroup.ROUTINE -> R.drawable.ic_grooming
}

fun PetType.icon() = when (this) {
    PetType.CAT -> R.drawable.ic_cat
    PetType.DOG -> R.drawable.ic_dog
}
