package com.gaoyun.feature_home_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gaoyun.common.theme.RoarTheme
import com.gaoyun.common.ui.PrimaryElevatedButton

@Composable
fun NoPetsState(
    userName: String,
    onAddPetButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Great, nice to meet you, ${userName}!\nNow you can add pets",
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.size(32.dp))

        PrimaryElevatedButton(
            text = "Add First Pet",
            onClick = onAddPetButtonClick
        )
    }
}

@Preview
@Composable
fun NoPetsStatePreview() {
    RoarTheme {
        NoPetsState("Tester") {}
    }
}