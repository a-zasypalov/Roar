package com.gaoyun.common.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BoxWithLoader(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    contentAlignment: androidx.compose.ui.Alignment = androidx.compose.ui.Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        contentAlignment = contentAlignment,
        modifier = modifier
    ) {
        AnimatedVisibility(
            modifier = Modifier
                .wrapContentSize(),
            visible = !isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            content()
        }
        Loader(isLoading = isLoading)
    }
}