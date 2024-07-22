package com.gaoyun.roar.ui.features.home.states

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.ui.common.composables.PrimaryElevatedButton
import com.gaoyun.roar.ui.common.composables.Spacer
import com.gaoyun.roar.ui.theme.RoarTheme
import com.gaoyun.roar.ui.theme.RoarThemePreview
import com.gaoyun.roar.util.Platform
import com.gaoyun.roar.util.PlatformNames
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.add_first_pet
import roar.sharedlib.generated.resources.ic_tab_home
import roar.sharedlib.generated.resources.import_backup_in_profile
import roar.sharedlib.generated.resources.nice_to_meet_you

@Composable
fun NoPetsState(
    userName: String,
    onAddPetButtonClick: () -> Unit,
    onUserDetailsClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.5f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(
                    WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
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

            Spacer(size = 24.dp)
            Text(
                text = stringResource(resource = Res.string.nice_to_meet_you, userName),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            PrimaryElevatedButton(
                text = stringResource(resource = Res.string.add_first_pet),
                onClick = onAddPetButtonClick
            )

            if (Platform.name == PlatformNames.Android) {
                Spacer(size = 16.dp)
                PrimaryElevatedButton(
                    text = stringResource(resource = Res.string.import_backup_in_profile),
                    onClick = onUserDetailsClick
                )
            }

            Spacer(size = 64.dp)
        }
    }
}

@Preview
@Composable
fun NoPetsStatePreview() {
    RoarThemePreview {
        NoPetsState("Tester", {}, {})
    }
}