@file:OptIn(ExperimentalComposeUiApi::class)

package com.gaoyun.common.ext

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.pluralStringResource
import com.gaoyun.common.DateUtils.monthsFromNow
import com.gaoyun.common.DateUtils.yearsFromNow
import com.gaoyun.common.R
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.PetWithInteractions
import com.gaoyun.roar.model.domain.withoutInteractions

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
