package com.gaoyun.feature_user_registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gaoyun.common.R
import com.gaoyun.common.composables.PrimaryElevatedButton
import com.gaoyun.common.composables.TextFormField
import com.gaoyun.common.composables.noRippleClickable
import com.gaoyun.common.theme.RoarThemePreview
import kotlinx.datetime.Clock

@Composable
fun UserRegistrationForm(
    onRegisterClick: (String) -> Unit,
    onRegisterTestClick: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    val termsAndConditionsUrl = stringResource(id = R.string.url_terms_and_conditions)
    val privacyPolicyUrl = stringResource(id = R.string.url_privacy_policy)
    var nameState by rememberSaveable { mutableStateOf("") }

    val timestampForTesting = Clock.System.now().epochSeconds

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.navigationBarsPadding(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxHeight()
        ) {
            Text(
                stringResource(id = R.string.registration_subtitle),
                modifier = Modifier.noRippleClickable {
                    if(Clock.System.now().epochSeconds - timestampForTesting > 10) {
                        onRegisterTestClick()
                    }
                }
            )

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

        Column {
            PrimaryElevatedButton(
                text = stringResource(id = R.string.register_or_login),
                onClick = { onRegisterClick(nameState) },
                modifier = Modifier.padding(bottom = 16.dp)
            )

            TextButton(
                onClick = { uriHandler.openUri(termsAndConditionsUrl) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(id = R.string.terms_and_conditions_button),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }

            TextButton(
                onClick = { uriHandler.openUri(privacyPolicyUrl) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(id = R.string.privacy_policy),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }

            com.gaoyun.common.composables.Spacer(size = 8.dp)
        }
    }
}

@Composable
@Preview
fun UserRegistrationFormPreview() {
    RoarThemePreview {
        UserRegistrationForm({}, {})
    }
}