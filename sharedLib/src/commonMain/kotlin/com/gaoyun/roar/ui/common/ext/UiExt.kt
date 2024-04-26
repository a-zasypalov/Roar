package com.gaoyun.roar.ui.common.ext

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.LanguageQualifier
import org.jetbrains.compose.resources.ResourceItem
import org.jetbrains.compose.resources.StringResource

@OptIn(ExperimentalResourceApi::class, InternalResourceApi::class)
internal fun getDrawableByName(idRes: String): DrawableResource {
    return DrawableResource(
        "drawable:$idRes", setOf(ResourceItem(setOf(), "drawable/$idRes.xml"))
    )
}

@OptIn(ExperimentalResourceApi::class, InternalResourceApi::class)
internal fun getStringByName(idRes: String): StringResource {
    return StringResource(
        "string:$idRes", idRes,
        setOf(
            ResourceItem(setOf(LanguageQualifier("de")), "values-de/strings.xml"),
            ResourceItem(setOf(LanguageQualifier("ru")), "values-ru/strings.xml"),
            ResourceItem(setOf(), "values/strings.xml"),
        )
    )
}