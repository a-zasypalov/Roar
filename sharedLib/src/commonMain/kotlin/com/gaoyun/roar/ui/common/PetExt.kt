package com.gaoyun.roar.ui.common

import androidx.compose.runtime.Composable
import com.gaoyun.roar.model.domain.Gender
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.PetWithInteractions
import com.gaoyun.roar.model.domain.withoutInteractions
import com.gaoyun.roar.util.SharedDateUtils.monthsFromNow
import com.gaoyun.roar.util.SharedDateUtils.yearsFromNow
import org.jetbrains.compose.resources.ExperimentalResourceApi
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.female
import roar.sharedlib.generated.resources.male

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

@OptIn(ExperimentalResourceApi::class)
fun Gender.toLocalizedStringId() = when (this) {
    Gender.MALE -> Res.string.male
    Gender.FEMALE -> Res.string.female
}