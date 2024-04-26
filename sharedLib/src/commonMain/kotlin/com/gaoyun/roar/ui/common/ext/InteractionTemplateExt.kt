package com.gaoyun.roar.ui.common.ext

import androidx.compose.runtime.Composable
import com.gaoyun.roar.model.domain.interactions.InteractionTemplate
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

/**
 * try-catch -> default getStringByName(type.toString())
 */
@OptIn(ExperimentalResourceApi::class)
@Throws(IllegalArgumentException::class)
@Composable
fun InteractionTemplate.getName(): String? {
//    val v = stringResource(getStringByName(name)) TODO: fix somehow
    return stringResource(getStringByName(type.toString()))
}