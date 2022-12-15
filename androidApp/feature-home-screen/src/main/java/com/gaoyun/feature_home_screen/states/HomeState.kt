package com.gaoyun.feature_home_screen.states

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gaoyun.common.DateUtils.monthsFromNow
import com.gaoyun.common.DateUtils.yearsFromNow
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
        item {
            Box(modifier = Modifier.size(WindowInsets.statusBars.asPaddingValues().calculateTopPadding()))
        }
        item {
            Header(userName = userName, onAddPetButtonClick = onAddPetButtonClick)
        }

        item {
            Spacer(size = 8.dp)
        }

//        if (pets.size == 1) {
//            item {
//                PetContainer(
//                    pet = pets.first(),
//                    interactions = state.interactions,
//                    showLastReminder = state.showLastReminder,
//                    onInteractionClick = { onEventSent(PetScreenContract.Event.InteractionClicked(it)) },
//                    onDeletePetClick = { onEventSent(PetScreenContract.Event.OnDeletePetClicked) },
//                    onEditPetClick = { onNavigationRequested(PetScreenContract.Effect.Navigation.ToEditPet(pet = pet)) },
//                    onInteractionCheckClicked = { reminderId, completed -> onEventSent(PetScreenContract.Event.OnInteractionCheckClicked(reminderId, completed)) },
//                    modifier = Modifier
//                        .verticalScroll(rememberScrollState())
//                        .padding(start = 8.dp, end = 8.dp)
//                        .fillMaxWidth()
//                )
//            }
//        } else {
            item {
                Text(
                    text = "Your pets",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
                )
            }

            items(pets) { pet ->
                PetCard(pet = pet, onPetCardClick = onPetCardClick)
            }
//        }
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
            .padding(start = 16.dp, end = 16.dp, top = 32.dp)
    ) {
        Text(
            text = "Hey, $userName",
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(size = 8.dp)

        FilledTonalButton(onClick = onAddPetButtonClick) {
            Icon(Icons.Filled.Pets, contentDescription = "")
            Spacer(size = 6.dp)
            Text("Add pet", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
private fun PetCard(
    pet: Pet,
    onPetCardClick: (petId: String) -> Unit
) {
    val context = LocalContext.current

    Surface(
        shape = MaterialTheme.shapes.large,
        tonalElevation = 8.dp,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Column(modifier = Modifier.clickable { onPetCardClick(pet.id) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = context.getDrawableByName(pet.avatar)),
                    contentDescription = pet.petType.toString(),
                    modifier = Modifier
                        .size(72.dp)
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = pet.name,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "${pet.birthday.yearsFromNow()} years ${pet.birthday.monthsFromNow()} months",
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