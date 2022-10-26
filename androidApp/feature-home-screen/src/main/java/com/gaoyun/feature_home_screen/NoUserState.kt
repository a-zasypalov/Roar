package com.gaoyun.feature_home_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.elevation
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gaoyun.common.theme.RoarTheme

@Composable
fun NoUserState(onRegisterButtonClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Register to get started")

        Spacer(modifier = Modifier.size(32.dp))

        Button(
            onClick = onRegisterButtonClick,
            elevation = elevation(
                defaultElevation = 2.dp,
                pressedElevation = 8.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                "Register",
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Preview
@Composable
fun NoUserStatePreview() {
    RoarTheme {
        NoUserState {

        }
    }
}