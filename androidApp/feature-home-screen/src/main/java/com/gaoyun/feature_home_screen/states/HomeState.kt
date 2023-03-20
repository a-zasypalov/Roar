package com.gaoyun.feature_home_screen.states

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.gaoyun.feature_home_screen.view.UserHomeHeader
import com.gaoyun.feature_pet_screen.view.InteractionCard
import com.gaoyun.feature_pet_screen.view.PetContainer
import com.gaoyun.roar.model.domain.*
import com.gaoyun.roar.model.domain.interactions.InteractionGroup
import com.gaoyun.roar.model.domain.interactions.InteractionType
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.util.toLocalDate
import kotlinx.datetime.*
import kotlin.time.Duration.Companion.hours

private val MAX_DATE = LocalDateTime(LocalDate.fromEpochDays(Int.MAX_VALUE), LocalTime(0, 0, 0))
private val MIN_DATE = LocalDateTime(LocalDate.fromEpochDays(0), LocalTime(0, 0, 0))

@Composable
fun HomeState(
    pets: List<PetWithInteractions>,
    showLastReminder: Boolean,
    remindersPerPet: Int,
    onAddPetButtonClick: () -> Unit,
    onPetCardClick: (petId: String) -> Unit,
    onInteractionClick: (petId: String, interactionId: String) -> Unit,
    onDeletePetClick: (PetWithInteractions) -> Unit,
    onEditPetClick: (PetWithInteractions) -> Unit,
    onInteractionCheckClicked: (pet: PetWithInteractions, interactionId: String, isChecked: Boolean, completionDateTime: LocalDateTime) -> Unit,
    onUserDetailsClick: () -> Unit
) {

    val remindersPerPetState = remember { mutableStateOf(remindersPerPet) }
    val shownInteractionsState = remember {
        mutableStateOf(
            pets.map { pet ->
                pet.id to pet.interactions.values
                    .flatten()
                    .flatMap { it.reminders }
                    .filter { !it.isCompleted }
                    .sortedBy { it.dateTime }
                    .take(remindersPerPetState.value)
            }
        )
    }

    val interactionsToShowUpdated = pets.map { pet -> pet.interactions.values.flatten() }
    val interactionsState = remember { mutableStateOf(pets.map { pet -> pet.interactions.values.flatten() }) }

    if (remindersPerPetState.value != remindersPerPet || interactionsState.value.flatten().size != interactionsToShowUpdated.flatten().size) {
        shownInteractionsState.value = pets.map { pet ->
            pet.id to pet.interactions.values
                .flatten()
                .flatMap { it.reminders }
                .filter { !it.isCompleted }
                .sortedBy { it.dateTime }
                .take(remindersPerPet)
        }
    }

    val sortedPetsState = pets.sortedBy { pet ->
        pet.interactions.values.flatten()
            .minOfOrNull { interaction ->
                interaction.reminders
                    .filter { reminder -> shownInteractionsState.value.flatMap { i -> i.second.map { r -> r.id } }.contains(reminder.id) }
                    .minOfOrNull { reminder -> reminder.dateTime } ?: MAX_DATE
            } ?: MIN_DATE
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        item {
            Box(modifier = Modifier.size(WindowInsets.statusBars.asPaddingValues().calculateTopPadding()))
        }
        item {
            UserHomeHeader(
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
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                )
            }

            items(sortedPetsState) { pet ->
                PetCard(
                    pet = pet,
                    showedInteractions = shownInteractionsState.value.firstOrNull { it.first == pet.id }?.second ?: listOf(),
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
private fun PetCard(
    pet: PetWithInteractions,
    showedInteractions: List<Reminder>,
    showLastReminder: Boolean,
    onPetCardClick: (petId: String) -> Unit,
    onInteractionClick: (String) -> Unit,
    onInteractionCheckClicked: (String, Boolean, java.time.LocalDateTime) -> Unit,
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
                        text = pet.ageText(),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            showedInteractions.map { reminder ->
                pet.interactions.values.flatten().firstOrNull { it.id == reminder.interactionId }?.let { interaction ->
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
        HomeState(listOf(
            Pet(
                petType = PetType.CAT,
                breed = "Colorpoint Shorthair",
                name = "Senior Android Developer",
                avatar = "ic_cat_15",
                userId = "123",
                birthday = Clock.System.now().toLocalDate(),
                isSterilized = false,
                gender = Gender.MALE,
                chipNumber = "123123456456",
                dateCreated = Clock.System.now().toLocalDate()
            ).withInteractions(
                mapOf(
                    InteractionGroup.CARE to
                            listOf(
                                InteractionWithReminders(
                                    petId = "",
                                    type = InteractionType.CUSTOM,
                                    name = "Interaction Name",
                                    group = InteractionGroup.CARE,
                                    isActive = true,
                                    reminders = listOf(
                                        Reminder(
                                            interactionId = "",
                                            dateTime = Clock.System.now().plus(1.hours).toLocalDateTime(TimeZone.currentSystemDefault())
                                        )
                                    )
                                )
                            )
                )
            ),
        ), false, 2, {}, {}, { _, _ -> }, {}, {}, { _, _, _, _ -> }, {})
    }
}