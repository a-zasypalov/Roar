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
import com.gaoyun.roar.ui.common.composables.PrimaryElevatedButton
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.login
import roar.sharedlib.generated.resources.register_or_login
import roar.sharedlib.generated.resources.register_to_get_started

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
            text = stringResource(resource = Res.string.register_to_get_started),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.size(32.dp))

        PrimaryElevatedButton(
            text = stringResource(resource = Res.string.register_or_login),
            onClick = onRegisterButtonClick
        )
        Spacer(modifier = Modifier.size(16.dp))

        PrimaryElevatedButton(
            text = stringResource(resource = Res.string.login),
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