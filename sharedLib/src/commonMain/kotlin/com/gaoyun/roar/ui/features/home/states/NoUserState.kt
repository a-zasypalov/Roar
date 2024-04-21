package com.gaoyun.roar.ui.features.home.states

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.ui.PrimaryElevatedButton

@Composable
fun NoUserState(
    onRegisterButtonClick: () -> Unit,
    onLoginButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Register to get started", //stringResource(id = CommonR.string.register_to_get_started),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.size(32.dp))

        PrimaryElevatedButton(
            text = "Register or Login", //stringResource(id = CommonR.string.register_or_login),
            onClick = onRegisterButtonClick
        )
        Spacer(modifier = Modifier.size(16.dp))

        PrimaryElevatedButton(
            text = "Login", //stringResource(id = CommonR.string.login),
            onClick = onLoginButtonClick
        )
    }
}

//@Preview
//@Composable
//fun NoUserStatePreview() {
//    RoarThemePreview {
//        NoUserState({}, {})
//    }
//}