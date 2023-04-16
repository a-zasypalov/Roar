package com.gaoyun.feature_user_registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gaoyun.common.R
import com.gaoyun.common.composables.PrimaryElevatedButton
import com.gaoyun.common.composables.TextFormField
import com.gaoyun.common.theme.RoarTheme

@Composable
fun UserRegistrationForm(
    onRegisterClick: (String) -> Unit
) {
    var nameState by rememberSaveable { mutableStateOf("") }

    Box(contentAlignment = Alignment.BottomCenter) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxHeight()
        ) {
            Text(stringResource(id = R.string.registration_subtitle))

            Spacer(modifier = Modifier.size(16.dp))

            TextFormField(
                text = nameState,
                leadingIcon = {
                    Icon(
                        Icons.Filled.Person,
                        stringResource(id = R.string.name),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                label = stringResource(id = R.string.name),
                onChange = {
                    nameState = it
                },
                imeAction = ImeAction.Done,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }

        PrimaryElevatedButton(
            text = stringResource(id = R.string.register_or_login),
            onClick = { onRegisterClick(nameState) },
            modifier = Modifier.padding(bottom = 32.dp)
        )
    }
}

@Composable
@Preview
fun UserRegistrationFormPreview() {
    RoarTheme {
        UserRegistrationForm {}
    }
}