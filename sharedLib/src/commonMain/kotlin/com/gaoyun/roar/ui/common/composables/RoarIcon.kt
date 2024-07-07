package com.gaoyun.roar.ui.common.composables

import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import com.gaoyun.roar.ui.common.ext.getDrawableByName
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun RoarIcon(
    icon: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(getDrawableByName(icon)),
        contentDescription = contentDescription,
        modifier = modifier,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
    )
}

@Composable
fun RoarIcon(
    icon: DrawableResource,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(icon),
        contentDescription = contentDescription,
        modifier = modifier,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
    )
}