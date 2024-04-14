package com.gaoyun.roar

import androidx.compose.ui.window.ComposeUIViewController
import com.gaoyun.roar.ui.App
import com.gaoyun.roar.ui.theme.colors.BlueColor

fun MainViewController() = ComposeUIViewController { App(BlueColor.LightColors, false) }