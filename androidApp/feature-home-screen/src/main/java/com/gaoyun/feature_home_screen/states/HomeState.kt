package com.gaoyun.feature_home_screen.states

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gaoyun.common.R
import com.gaoyun.common.ext.ageText
import com.gaoyun.common.theme.RoarTheme
import com.gaoyun.common.ui.Spacer
import com.gaoyun.common.ui.getDrawableByName
import com.gaoyun.feature_pet_screen.view.InteractionCard
import com.gaoyun.feature_pet_screen.view.PetContainer
import com.gaoyun.roar.model.domain.PetWithInteractions
import com.gaoyun.roar.model.domain.User
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime

@Composable
fun HomeState(
    user: User,
    pets: List<PetWithInteractions>,
    showLastReminder: Boolean,
    onAddPetButtonClick: () -> Unit,
    onPetCardClick: (petId: String) -> Unit,
    onInteractionClick: (petId: String, interactionId: String) -> Unit,
    onDeletePetClick: (PetWithInteractions) -> Unit,
    onEditPetClick: (PetWithInteractions) -> Unit,
    onInteractionCheckClicked: (pet: PetWithInteractions, interactionId: String, isChecked: Boolean, completionDateTime: LocalDateTime) -> Unit,
    onUserDetailsClick: () -> Unit
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
            Header(
                userName = user.name,
                onAddPetButtonClick = onAddPetButtonClick,
                onUserButtonButtonClick = onUserDetailsClick,
            )
        }

        item {
            Spacer(size = 8.dp)
        }

        if (pets.size == 1) {
            item {
                val pet = pets.first()
                PetContainer(
                    isPartOfAnotherScreen = true,
                    pet = pet,
                    showLastReminder = showLastReminder,
                    onInteractionClick = { interactionId -> onInteractionClick(pet.id, interactionId) },
                    onDeletePetClick = { onDeletePetClick(pet) },
                    onEditPetClick = { onEditPetClick(pet) },
                    onInteractionCheckClicked = { interactionId, isChecked, completionDateTime ->
                        onInteractionCheckClicked(
                            pet,
                            interactionId,
                            isChecked,
                            completionDateTime.toKotlinLocalDateTime()
                        )
                    },
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .fillMaxWidth()
                )
            }
        } else {
            item {
                Text(
                    text = stringResource(id = R.string.your_pets),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
                )
            }

            items(pets) { pet ->
                PetCard(
                    pet = pet,
                    onPetCardClick = onPetCardClick,
                    showLastReminder = showLastReminder,
                    onInteractionClick = { interactionId -> onInteractionClick(pet.id, interactionId) },
                    onInteractionCheckClicked = { interactionId, isChecked, completionDateTime ->
                        onInteractionCheckClicked(
                            pet,
                            interactionId,
                            isChecked,
                            completionDateTime.toKotlinLocalDateTime()
                        )
                    }
                )
            }

            item { Spacer(size = 132.dp) }
        }
    }
}

@Composable
private fun Header(
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

@Composable
private fun PetCard(
    pet: PetWithInteractions,
    showLastReminder: Boolean,
    onPetCardClick: (petId: String) -> Unit,
    onInteractionClick: (String) -> Unit,
    onInteractionCheckClicked: (String, Boolean, java.time.LocalDateTime) -> Unit,
) {
    val context = LocalContext.current
    val showedInteractions = remember {
        mutableStateOf(pet.interactions
            .flatMap { it.reminders }
            .filter { !it.isCompleted }
            .sortedBy { it.dateTime }
            .take(2))
    }

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
                        text = pet.ageText(),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            showedInteractions.value.map { reminder ->
                pet.interactions.firstOrNull { it.id == reminder.interactionId }?.let { interaction ->
                    InteractionCard(
                        interaction = interaction,
                        showLastReminder = showLastReminder,
                        elevation = 64.dp,
                        shape = MaterialTheme.shapes.medium,
                        onClick = onInteractionClick,
                        onInteractionCheckClicked = onInteractionCheckClicked,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                            .fillMaxWidth()
                    )
                }
            }
            Spacer(size = 0.dp)
        }
    }
}

@Preview
@Composable
fun HomeStatePreview() {
    RoarTheme {
        HomeState(User(name = "Tester"), emptyList(), false, {}, {}, { _, _ -> }, {}, {}, { _, _, _, _ -> }, {})
    }
}