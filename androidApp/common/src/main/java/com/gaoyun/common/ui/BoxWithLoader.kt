package com.gaoyun.common.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BoxWithLoader(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    contentAlignment: androidx.compose.ui.Alignment = androidx.compose.ui.Alignment.TopStart,
    content: @Composable () -> Unit,
) {
    Box(
        contentAlignment = contentAlignment,
        modifier = modifier
    ) {
        content()
        Loader(isLoading = isLoading)
    }
}