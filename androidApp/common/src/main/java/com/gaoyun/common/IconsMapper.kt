package com.gaoyun.common

import com.gaoyun.roar.model.domain.interactions.InteractionGroup
import com.gaoyun.roar.model.domain.interactions.InteractionType

fun InteractionType.icon() = when(this) {
    InteractionType.HEALTH_CHECK -> R.drawable.ic_health_check
    InteractionType.FLEES -> R.drawable.ic_drops
    InteractionType.DEWORMING -> R.drawable.ic_pills
    InteractionType.CUSTOM -> R.drawable.ic_care
}

fun InteractionGroup.icon() = when(this) {
    InteractionGroup.HEALTH -> R.drawable.ic_medical
    InteractionGroup.CARE -> R.drawable.ic_care
    InteractionGroup.ROUTINE -> R.drawable.ic_grooming
}