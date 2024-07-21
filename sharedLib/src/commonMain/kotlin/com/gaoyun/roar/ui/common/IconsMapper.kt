package com.gaoyun.roar.ui.common

import com.gaoyun.roar.model.domain.PetType
import com.gaoyun.roar.model.domain.interactions.InteractionGroup
import com.gaoyun.roar.model.domain.interactions.InteractionType
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.ic_care
import roar.sharedlib.generated.resources.ic_cat
import roar.sharedlib.generated.resources.ic_dog
import roar.sharedlib.generated.resources.ic_drops
import roar.sharedlib.generated.resources.ic_grooming
import roar.sharedlib.generated.resources.ic_health_check
import roar.sharedlib.generated.resources.ic_medical
import roar.sharedlib.generated.resources.ic_nails
import roar.sharedlib.generated.resources.ic_pills
import roar.sharedlib.generated.resources.ic_soap
import roar.sharedlib.generated.resources.ic_vaccine

fun InteractionType.icon() = when (this) {
    InteractionType.HEALTH_CHECK -> Res.drawable.ic_health_check
    InteractionType.FLEES -> Res.drawable.ic_drops
    InteractionType.DEWORMING -> Res.drawable.ic_pills
    InteractionType.BATHING -> Res.drawable.ic_soap
    InteractionType.VACCINATION -> Res.drawable.ic_vaccine
    InteractionType.PILLS -> Res.drawable.ic_pills
    InteractionType.GROOMING -> Res.drawable.ic_grooming
    InteractionType.NAILS -> Res.drawable.ic_nails
    InteractionType.CUSTOM -> Res.drawable.ic_care
}

fun InteractionGroup.icon() = when (this) {
    InteractionGroup.HEALTH -> Res.drawable.ic_medical
    InteractionGroup.CARE -> Res.drawable.ic_care
    InteractionGroup.ROUTINE -> Res.drawable.ic_grooming
}

fun PetType.icon() = when (this) {
    PetType.CAT -> Res.drawable.ic_cat
    PetType.DOG -> Res.drawable.ic_dog
}
