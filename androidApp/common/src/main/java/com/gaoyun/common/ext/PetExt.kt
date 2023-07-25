package com.gaoyun.common.ext

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import com.gaoyun.common.R
import com.gaoyun.roar.model.domain.Gender
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.PetWithInteractions
import com.gaoyun.roar.model.domain.withoutInteractions
import com.gaoyun.roar.util.SharedDateUtils.monthsFromNow
import com.gaoyun.roar.util.SharedDateUtils.yearsFromNow

@Composable
fun PetWithInteractions.ageText() = withoutInteractions().ageText()

@Composable
fun Pet.ageText() = StringBuilder().apply {
    if (birthday.yearsFromNow() > 0) {
        append(birthday.yearsFromNow())
        append(" ")
        append(pluralStringResource(id = R.plurals.years, count = birthday.yearsFromNow()))
        append(" ")
    }
    append(birthday.monthsFromNow())
    append(" ")
    append(pluralStringResource(id = R.plurals.months, count = birthday.monthsFromNow()))
}.toString()

fun Gender.toLocalizedStringId() = when (this) {
    Gender.MALE -> R.string.male
    Gender.FEMALE -> R.string.female
}