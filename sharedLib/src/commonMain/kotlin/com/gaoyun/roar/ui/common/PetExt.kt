package com.gaoyun.roar.ui.common

import androidx.compose.runtime.Composable
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
//        append(pluralStringResource(id = R.plurals.years, count = birthday.yearsFromNow()))
        append("years")
        append(" ")
    }
    append(birthday.monthsFromNow())
    append(" ")
//    append(pluralStringResource(id = R.plurals.months, count = birthday.monthsFromNow()))
    append("months")
}.toString()

fun Gender.toLocalizedStringId() = when (this) {
    Gender.MALE -> "Male" //R.string.male
    Gender.FEMALE -> "Female" //R.string.female
}