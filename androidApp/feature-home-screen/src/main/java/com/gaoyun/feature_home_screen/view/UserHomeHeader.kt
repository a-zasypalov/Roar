package com.gaoyun.feature_home_screen.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gaoyun.common.R
import com.gaoyun.common.theme.RoarTheme
import com.gaoyun.common.composables.Spacer

@OptIn(ExperimentalMaterial3Api::class)
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
            ) {
                Icon(Icons.Filled.Pets, contentDescription = null)
                Spacer(size = 6.dp)
                Text(stringResource(id = R.string.add_pet), style = MaterialTheme.typography.titleMedium)
            }

            Image(
                painter = painterResource(id = R.drawable.ic_tab_home),
                contentDescription = "icon",
                modifier = Modifier
                    .weight(0.24f)
                    .padding(horizontal = 24.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inverseSurface)
            )

            Surface(
                tonalElevation = 8.dp,
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
                    Text(stringResource(R.string.profile), style = MaterialTheme.typography.titleMedium)
                }
            }
        }

        Spacer(size = 12.dp)

        Divider()
    }
}

@Preview
@Composable
fun UserHomeHeaderPreview() {
    RoarTheme {
        UserHomeHeader({}, {})
    }
}