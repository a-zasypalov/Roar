package com.gaoyun.roar.ui.features.registration

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.roar.ui.theme.RoarTheme
import kotlinx.datetime.Clock

@Composable
fun UserRegistrationForm(
    onRegisterClick: () -> Unit,
    onRegisterTestClick: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    val termsAndConditionsUrl = "Terms and conditions" //stringResource(id = R.string.url_terms_and_conditions)
    val privacyPolicyUrl = "Privacy Policy" //stringResource(id = R.string.url_privacy_policy)

    val timestampForTesting = Clock.System.now().epochSeconds

    Box(modifier = Modifier.navigationBarsPadding()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier.size(
                    WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 48.dp
                )
            )

            Surface(
                tonalElevation = RoarTheme.IMAGE_ITEM_ELEVATION,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraLarge)
                    .size(160.dp)
            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.ic_tab_home),
//                    contentDescription = "icon",
//                    modifier = Modifier.padding(16.dp),
//                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inverseSurface)
//                )
            }

            com.gaoyun.roar.ui.Spacer(size = 24.dp)
            Text(
                text = "Roar", //stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable {
                    if (Clock.System.now().epochSeconds - timestampForTesting > 10) {
                        onRegisterTestClick()
                    }
                }
            )
        }

        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.BottomCenter)
        ) {
            ElevatedButton(
                onClick = { onRegisterClick() },
                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 8.dp
                ),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                ),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.btn_google),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .weight(2f)
//                        .padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
//                )
                Text(
                    "Register or Login", //stringResource(id = R.string.register_or_login),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .weight(8f)
                        .fillMaxWidth(),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.weight(2f))
            }

            TextButton(
                onClick = { uriHandler.openUri(termsAndConditionsUrl) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Terms and Conditions", //stringResource(id = R.string.terms_and_conditions_button),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }

            TextButton(
                onClick = { uriHandler.openUri(privacyPolicyUrl) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Privacy policy", //stringResource(id = R.string.privacy_policy),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }

            com.gaoyun.roar.ui.Spacer(size = 8.dp)
        }
    }
}

//@Composable
//@Preview(device = Devices.PIXEL)
//fun UserRegistrationFormPreview() {
//    RoarThemePreview {
//        UserRegistrationForm({}, {})
//    }
//}