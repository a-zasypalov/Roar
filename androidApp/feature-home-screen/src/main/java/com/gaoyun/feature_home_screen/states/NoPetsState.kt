package com.gaoyun.feature_home_screen.states

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gaoyun.common.theme.RoarTheme
import com.gaoyun.common.composables.PrimaryElevatedButton
import com.gaoyun.common.composables.Spacer
import com.gaoyun.common.R as CommonR

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
            Box(modifier = Modifier.size(WindowInsets.statusBars.asPaddingValues().calculateTopPadding()))

            Surface(
                tonalElevation = 16.dp,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraLarge)
                    .size(160.dp)
            ) {
                Image(
                    painter = painterResource(id = com.gaoyun.common.R.drawable.ic_tab_home),
                    contentDescription = "icon",
                    modifier = Modifier.padding(16.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inverseSurface)
                )
            }

            Spacer(size = 24.dp)
            Text(text = stringResource(id = com.gaoyun.common.R.string.nice_to_meet_you, userName) , style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            PrimaryElevatedButton(
                text = stringResource(id = CommonR.string.add_first_pet),
                onClick = onAddPetButtonClick
            )

            Spacer(size = 16.dp)

            PrimaryElevatedButton(
                text = stringResource(id = CommonR.string.import_backup_in_profile),
                onClick = onUserDetailsClick
            )

            Spacer(size = 64.dp)
        }
    }
}

@Preview
@Composable
fun NoPetsStatePreview() {
    RoarTheme {
        NoPetsState("Tester", {}, {})
    }
}