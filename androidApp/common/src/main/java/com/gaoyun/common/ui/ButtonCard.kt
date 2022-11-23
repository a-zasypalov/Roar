package com.gaoyun.common.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
@SuppressLint("ModifierParameter")
fun ButtonCard(
    modifier: Modifier = Modifier.padding(8.dp),
    shape: Shape = CardDefaults.shape,
    elevation: CardElevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    content: @Composable () -> Unit
) {
    Card(
        shape = shape,
        elevation = elevation,
        modifier = modifier,
    ) {
        content()
    }
}