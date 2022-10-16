package com.gaoyun.common.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Loader(isLoading: Boolean) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            modifier = Modifier
                .wrapContentSize(),
            visible = isLoading,
            enter = EnterTransition.None,
            exit = fadeOut()
        ) {
            CircularProgressIndicator()
        }
    }
}