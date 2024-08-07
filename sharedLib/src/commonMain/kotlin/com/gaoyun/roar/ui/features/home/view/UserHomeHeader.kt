package com.gaoyun.roar.ui.features.home.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.ui.common.composables.Spacer
import com.gaoyun.roar.ui.theme.RoarTheme
import com.gaoyun.roar.ui.theme.RoarThemePreview
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.add_pet
import roar.sharedlib.generated.resources.ic_tab_home
import roar.sharedlib.generated.resources.profile

@Composable
internal fun UserHomeHeader(
    onAddPetButtonClick: () -> Unit,
    onUserButtonButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            FilledTonalButton(
                onClick = onAddPetButtonClick,
                modifier = Modifier.weight(0.38f),
                contentPadding = PaddingValues(all = 8.dp),
            ) {
                Icon(Icons.Filled.Pets, contentDescription = null)
                Spacer(size = 6.dp)
                Text(
                    stringResource(resource = Res.string.add_pet),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Image(
                painter = painterResource(Res.drawable.ic_tab_home),
                contentDescription = "icon",
                modifier = Modifier
                    .weight(0.24f)
                    .padding(horizontal = 24.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inverseSurface)
            )

            Surface(
                tonalElevation = RoarTheme.CONTENT_CARD_ELEVATION,
                modifier = Modifier.weight(0.38f),
                shape = CircleShape,
                onClick = onUserButtonButtonClick,
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Icon(Icons.Filled.Person, contentDescription = null)
                    Spacer(size = 6.dp)
                    Text(
                        stringResource(Res.string.profile),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }

        Spacer(size = 12.dp)

        HorizontalDivider()
    }
}

@Preview
@Composable
fun UserHomeHeaderPreview() {
    RoarThemePreview {
        UserHomeHeader({}, {})
    }
}