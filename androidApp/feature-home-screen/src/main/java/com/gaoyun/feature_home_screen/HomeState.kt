package com.gaoyun.feature_home_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gaoyun.common.R
import com.gaoyun.common.theme.RoarTheme
import com.gaoyun.common.ui.PrimaryElevatedButton
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.PetType

@Composable
fun HomeState(
    userName: String,
    pets: List<Pet>,
    onAddPetButtonClick: () -> Unit,
    onPetCardClick: (pet: Pet) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Text(
                text = "Super! Welcome $userName",
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.size(32.dp))

            PrimaryElevatedButton(
                text = "Add Pet",
                onClick = onAddPetButtonClick
            )

            Spacer(modifier = Modifier.size(8.dp))
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
                            onPetCardClick(pet)
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