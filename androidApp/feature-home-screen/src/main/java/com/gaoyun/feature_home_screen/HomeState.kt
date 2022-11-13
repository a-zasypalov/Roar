package com.gaoyun.feature_home_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gaoyun.common.R
import com.gaoyun.common.theme.RoarTheme
import com.gaoyun.common.ui.Spacer
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.PetType

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
        item {
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
            }

            Spacer(size = 8.dp)
        }
        items(pets) { pet ->
            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp), elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onPetCardClick(pet.id)
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when (pet.petType) {
                        PetType.CAT -> {
                            Image(
                                painter = painterResource(id = R.drawable.ic_cat),
                                contentDescription = "cat",
                                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface),
                                modifier = Modifier.size(42.dp)
                            )
                        }
                        PetType.DOG -> {
                            Image(
                                painter = painterResource(id = R.drawable.ic_dog),
                                contentDescription = "dog",
                                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface),
                                modifier = Modifier.size(42.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.size(8.dp))

                    Text(pet.name, color = MaterialTheme.colorScheme.onSurface)
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