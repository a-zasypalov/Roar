package com.gaoyun.roar.ui.common.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LabelledCheckBox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit),
    label: String,
    modifier: Modifier = Modifier,
    verticalPadding: Dp = 12.dp,
    horizontalPadding: Dp = 16.dp,
    spacerSize: Dp = 6.dp
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .platformStyleClickable(
                indication = rememberRipple(color = MaterialTheme.colorScheme.primary),
                onClick = { onCheckedChange(!checked) }
            )
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null
        )

        Spacer(Modifier.size(spacerSize))

        AnimatedContent(
            targetState = label,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "LabelledCheckBoxAnimation"
        ) { targetLabel ->
            Text(
                text = targetLabel,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}