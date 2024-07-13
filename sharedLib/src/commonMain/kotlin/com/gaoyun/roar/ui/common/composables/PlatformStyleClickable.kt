package com.gaoyun.roar.ui.common.composables

import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.gaoyun.roar.util.Platform
import com.gaoyun.roar.util.PlatformNames

@Composable
fun Modifier.platformStyleClickable(
    enabled: Boolean = true,
    indication: Indication? = if (Platform.name == PlatformNames.IOS) null else LocalIndication.current,
    onClick: () -> Unit
): Modifier = composed {
    clickable(
        enabled = enabled,
        indication = indication,
        interactionSource = remember { MutableInteractionSource() },
        onClick = onClick
    )
}