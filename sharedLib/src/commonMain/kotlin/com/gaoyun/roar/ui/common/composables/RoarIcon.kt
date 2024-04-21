package com.gaoyun.roar.ui.common.composables

import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun RoarIcon(
    icon: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(DrawableResource(icon)), //TODO: painterResource(id = icon),
        contentDescription = contentDescription,
        modifier = modifier,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
    )
}