package com.gaoyun.roar.ui.features.registration

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.roar.ui.common.composables.platformStyleClickable
import com.gaoyun.roar.ui.theme.RoarTheme
import com.gaoyun.roar.ui.theme.RoarThemePreview
import com.gaoyun.roar.util.Platform
import com.gaoyun.roar.util.PlatformNames
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.app_name
import roar.sharedlib.generated.resources.btn_apple
import roar.sharedlib.generated.resources.btn_google
import roar.sharedlib.generated.resources.continue_with_apple
import roar.sharedlib.generated.resources.ic_tab_home
import roar.sharedlib.generated.resources.privacy_policy
import roar.sharedlib.generated.resources.register_or_login
import roar.sharedlib.generated.resources.terms_and_conditions_button
import roar.sharedlib.generated.resources.url_privacy_policy
import roar.sharedlib.generated.resources.url_terms_and_conditions
import com.gaoyun.roar.ui.common.composables.Spacer as SpacerRoar

enum class RegistrationType {
    Google, Apple
}

@Composable
fun UserRegistrationForm(
    onRegisterClick: (RegistrationType) -> Unit,
    onRegisterTestClick: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    val termsAndConditionsUrl = stringResource(resource = Res.string.url_terms_and_conditions)
    val privacyPolicyUrl = stringResource(resource = Res.string.url_privacy_policy)

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
                Image(
                    painter = painterResource(Res.drawable.ic_tab_home),
                    contentDescription = "icon",
                    modifier = Modifier.padding(16.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inverseSurface)
                )
            }

            com.gaoyun.roar.ui.common.composables.Spacer(size = 24.dp)
            Text(
                text = stringResource(resource = Res.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.platformStyleClickable {
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
                onClick = { onRegisterClick(RegistrationType.Google) },
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
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Image(
                        painter = painterResource(Res.drawable.btn_google),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp).padding(bottom = 2.dp)
                    )
                    Text(
                        stringResource(resource = Res.string.register_or_login),
                        fontSize = 16.sp,
                    )
                }
            }

            if(Platform.name == PlatformNames.IOS) {
                ElevatedButton(
                    onClick = { onRegisterClick(RegistrationType.Apple) },
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
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 16.dp)
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.btn_apple),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp).padding(bottom = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            stringResource(Res.string.continue_with_apple),
                            fontSize = 16.sp,
                        )
                    }
                }
            }

            TextButton(
                onClick = { uriHandler.openUri(termsAndConditionsUrl) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(resource = Res.string.terms_and_conditions_button),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }

            TextButton(
                onClick = { uriHandler.openUri(privacyPolicyUrl) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(resource = Res.string.privacy_policy),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }

            SpacerRoar(size = 8.dp)
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