package com.gaoyun.roar.ui.common.ext

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.ResourceItem

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