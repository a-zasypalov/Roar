package com.gaoyun.feature_home_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gaoyun.common.theme.RoarTheme
import com.gaoyun.common.ui.Spacer
import com.gaoyun.common.ui.getDrawableByName
import com.gaoyun.roar.model.domain.Pet

@Composable
fun HomeState(
    userName: String,
    pets: List<Pet>,
    onAddPetButtonClick: () -> Unit,
    onPetCardClick: (petId: String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        item { Header(userName = userName, onAddPetButtonClick = onAddPetButtonClick) }
        items(pets) { pet -> PetCard(pet = pet, onPetCardClick = onPetCardClick) }
    }
}

@Composable
private fun Header(
    userName: String,
    onAddPetButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Hey, $userName",
            style = MaterialTheme.typography.displayMedium,
        )

        Spacer(size = 8.dp)

        FilledTonalButton(onClick = onAddPetButtonClick) {
            Icon(Icons.Filled.Pets, contentDescription = "")
            Spacer(size = 6.dp)
            Text("Add pet", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(size = 8.dp)
    }
}

@Composable
private fun PetCard(
    pet: Pet,
    onPetCardClick: (petId: String) -> Unit
) {
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Column(modifier = Modifier.clickable { onPetCardClick(pet.id) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = context.getDrawableByName(pet.avatar)),
                    contentDescription = pet.petType.toString(),
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.95f))
                        .padding(12.dp)
                )

                Spacer(modifier = Modifier.size(16.dp))

                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = pet.name,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "${pet.birthday}",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeStatePreview() {
    RoarTheme {
        HomeState("Tester", emptyList(), {}, {})
    }
}