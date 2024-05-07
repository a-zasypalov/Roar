package com.gaoyun.roar.ui.common.ext

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.LanguageQualifier
import org.jetbrains.compose.resources.ResourceItem
import org.jetbrains.compose.resources.StringResource

@OptIn(InternalResourceApi::class)
internal fun getDrawableByName(idRes: String): DrawableResource {
    return DrawableResource(
        "drawable:$idRes",
        setOf(
            ResourceItem(
                setOf(),
                path = "composeResources/roar.sharedlib.generated.resources/drawable/$idRes.xml", -1, -1
            ),
        )
    )
}

//@OptIn(InternalResourceApi::class)
//internal fun getStringByName(idRes: String): StringResource {
//    return StringResource(
//        "string:$idRes", idRes,
//        setOf(
//            ResourceItem(setOf(LanguageQualifier("de")), "composeResources/roar.sharedlib.generated.resources/values-de/strings.xml", -1, -1),
//            ResourceItem(setOf(LanguageQualifier("ru")), "composeResources/roar.sharedlib.generated.resources/values-ru/strings.xml", -1, -1),
//            ResourceItem(setOf(), "composeResources/roar.sharedlib.generated.resources/values/strings.xml", -1, -1),
//        )
//    )
//}