package com.gaoyun.feature_home_screen.states

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gaoyun.common.theme.RoarTheme
import com.gaoyun.common.composables.PrimaryElevatedButton
import com.gaoyun.common.R as CommonR

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
            text = stringResource(id = CommonR.string.register_to_get_started),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.size(32.dp))

        PrimaryElevatedButton(
            text = stringResource(id = CommonR.string.register_or_login),
            onClick = onRegisterButtonClick
        )
        Spacer(modifier = Modifier.size(16.dp))

        PrimaryElevatedButton(
            text = stringResource(id = CommonR.string.login),
            onClick = onLoginButtonClick
        )
    }
}

@Preview
@Composable
fun NoUserStatePreview() {
    RoarTheme {
        NoUserState ({}, {})
    }
}