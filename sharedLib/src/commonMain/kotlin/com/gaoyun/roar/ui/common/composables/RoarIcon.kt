package com.gaoyun.roar.ui.common.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
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
    Box(modifier = Modifier.size(64.dp)) //TODO: Remove
//    Image(
//        painter = painterResource(DrawableResource(icon)), //TODO: painterResource(id = icon),
//        contentDescription = contentDescription,
//        modifier = modifier,
//        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
//    )
}