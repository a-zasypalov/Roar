package com.gaoyun.feature_home_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gaoyun.common.theme.RoarTheme
import com.gaoyun.common.ui.PrimaryElevatedButton

@Composable
fun NoUserState(onRegisterButtonClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Register to get started",
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.size(32.dp))

        PrimaryElevatedButton(
            text = "Register",
            onClick = onRegisterButtonClick
        )
    }
}

@Preview
@Composable
fun NoUserStatePreview() {
    RoarTheme {
        NoUserState {}
    }
}