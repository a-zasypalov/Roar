package com.gaoyun.roar.ui.features.home.view

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
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.ui.Spacer
import com.gaoyun.roar.ui.theme.RoarTheme

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
                    "Add Pet", //stringResource(id = R.string.add_pet),
                    style = MaterialTheme.typography.titleMedium
                )
            }

//            Image(
//                painter = painterResource(id = R.drawable.ic_tab_home),
//                contentDescription = "icon",
//                modifier = Modifier
//                    .weight(0.24f)
//                    .padding(horizontal = 24.dp),
//                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inverseSurface)
//            )

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
                        "Profile", ////stringResource(R.string.profile),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }

        Spacer(size = 12.dp)

        Divider()
    }
}

//@Preview
//@Composable
//fun UserHomeHeaderPreview() {
//    RoarThemePreview {
//        UserHomeHeader({}, {})
//    }
//}