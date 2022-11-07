package com.gaoyun.common.ui

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurfaceScaffold(content: @Composable () -> Unit) {
    Scaffold {
        Surface(color = MaterialTheme.colorScheme.surface) {
            content()
        }
    }
}