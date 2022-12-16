package com.gaoyun.common.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource

@Composable
fun RoarIcon(
    @DrawableRes icon: Int,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = icon),
        contentDescription = contentDescription,
        modifier = modifier,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
    )
}