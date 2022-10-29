package com.gaoyun.feature_home_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gaoyun.common.theme.RoarTheme
import com.gaoyun.common.ui.PrimaryRaisedButton
import com.gaoyun.roar.model.domain.Pet

@Composable
fun HomeState(
    userName: String,
    pets: List<Pet>,
    onAddPetButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Super! Welcome $userName and ${pets.joinToString { it.name }}",
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.size(32.dp))

        PrimaryRaisedButton(
            text = "Add Pet",
            onClick = onAddPetButtonClick
        )
    }
}

@Preview
@Composable
fun HomeStatePreview() {
    RoarTheme {
        HomeState("Tester", emptyList()) {}
    }
}