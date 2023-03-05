package com.gaoyun.feature_home_screen.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gaoyun.common.R
import com.gaoyun.common.ui.Spacer

@Composable
internal fun UserHomeHeader(
    userName: String,
    onAddPetButtonClick: () -> Unit,
    onUserButtonButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 32.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.hey, userName),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            IconButton(onClick = onUserButtonButtonClick, modifier = Modifier.padding(top = 12.dp)) {
                Icon(
                    Icons.Default.Person, contentDescription = "", modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                )
            }
        }

        Spacer(size = 8.dp)

        FilledTonalButton(onClick = onAddPetButtonClick) {
            Icon(Icons.Filled.Pets, contentDescription = "")
            Spacer(size = 6.dp)
            Text(stringResource(id = R.string.add_pet), style = MaterialTheme.typography.titleMedium)
        }
    }
}