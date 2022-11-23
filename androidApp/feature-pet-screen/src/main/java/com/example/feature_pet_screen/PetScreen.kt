package com.example.feature_pet_screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.example.pet_screen.R
import com.gaoyun.common.NavigationKeys
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.theme.RoarTheme
import com.gaoyun.common.ui.*
import com.gaoyun.roar.model.domain.Gender
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.PetType
import com.gaoyun.roar.model.domain.Reminder
import com.gaoyun.roar.model.domain.interactions.InteractionGroup
import com.gaoyun.roar.model.domain.interactions.InteractionType
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.pet_screen.PetScreenContract
import com.gaoyun.roar.presentation.pet_screen.PetScreenViewModel
import com.gaoyun.roar.util.toLocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.androidx.compose.getViewModel
import kotlin.time.Duration.Companion.hours

@Composable
fun PetScreenDestination(
    navHostController: NavHostController,
    petId: String
) {
    val viewModel: PetScreenViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_CREATE) {
            viewModel.buildScreenState(petId)
        }
    }

    PetScreen(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is PetScreenContract.Effect.Navigation.ToInteractionTemplates -> navigationEffect.petId?.let { petId ->
                    navHostController.navigate("${NavigationKeys.RouteGlobal.ADD_REMINDER}/$petId")
                }
                is PetScreenContract.Effect.Navigation.NavigateBack -> navHostController.navigateUp()
            }
        },
    )

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PetScreen(
    state: PetScreenContract.State,
    effectFlow: Flow<PetScreenContract.Effect>,
    onEventSent: (event: PetScreenContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: PetScreenContract.Effect.Navigation) -> Unit,
) {

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow.onEach { effect ->
            when (effect) {
                is PetScreenContract.Effect.Navigation.ToInteractionTemplates -> onNavigationRequested(effect)
                is PetScreenContract.Effect.Navigation.NavigateBack -> onNavigationRequested(effect)
                else -> {}
            }
        }.collect()
    }

    SurfaceScaffold(
        floatingActionButton = {
            RoarExtendedFloatingActionButton(
                icon = Icons.Filled.Add,
                contentDescription = "Add reminder",
                text = "Reminder",
                onClick = { onEventSent(PetScreenContract.Event.AddReminderButtonClicked(state.pet?.id)) })
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Box {
            state.pet?.let { pet ->
                PetContainer(pet = pet, state.interactions)
            }

            Loader(isLoading = state.isLoading)
        }
    }
}

@Composable
private fun PetContainer(pet: Pet, interactions: List<InteractionWithReminders>) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp)
            .fillMaxWidth()
    ) {
        item {
            PetHeader(
                pet = pet, modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, bottom = 8.dp)
            )
        }
        item {
            Text(
                text = "Reminders",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
            )
        }
        items(interactions) { interaction ->
            Surface(
                tonalElevation = 4.dp,
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Icon(
                            Icons.Filled.Alarm,
                            "",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(4.dp)
                        )
                        Spacer(size = 12.dp)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                interaction.name,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            val repeatText = interaction.repeatConfig?.toString() ?: "No repeat"
                            Text(
                                repeatText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(size = 8.dp)

                    interaction.reminders
                        .filter { !it.isCompleted || it.dateTime > Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) }
                        .maxByOrNull { it.dateTime }?.let { nextReminder ->
                            LabelledCheckBox(
                                checked = !nextReminder.isCompleted,
                                label = "Next: ${nextReminder.dateTime.date} at 09:00",
                                modifier = Modifier.fillMaxWidth(),
                                verticalPadding = 16.dp,
                                horizontalPadding = 20.dp,
                                spacerSize = 14.dp,
                                onCheckedChange = {}
                            )
                        } ?: Text(
                        text = "No active reminder",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
            }
        }

        item { Spacer(size = 120.dp) }
    }
}

@Composable
private fun PetHeader(
    pet: Pet,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isSterilized = remember { if (pet.isSterilized) "sterilized" else "not sterilized" }

    Column(
        modifier = modifier.padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Surface(
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxHeight(),
            shape = CircleShape,
        ) {
            Image(
                painter = painterResource(id = context.getDrawableByName(pet.avatar)),
                contentDescription = pet.name,
                modifier = Modifier
                    .height(72.dp)
                    .padding(all = 12.dp)
            )
        }

        Spacer(8.dp)

        Text(
            text = pet.name,
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(size = 16.dp)

        Card(
            elevation = CardDefaults.elevatedCardElevation(0.dp),
            shape = MaterialTheme.shapes.large,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                TextWithIconBulletPoint(icon = Icons.Filled.Cake, pet.birthday.toString())

                when (pet.gender) {
                    Gender.MALE -> TextWithIconBulletPoint(icon = Icons.Filled.Male, "Male, $isSterilized")
                    Gender.FEMALE -> TextWithIconBulletPoint(icon = Icons.Filled.Female, "Female, $isSterilized")
                }

                TextWithIconBulletPoint(icon = Icons.Filled.Pets, pet.breed)

                if (pet.chipNumber.isNotEmpty()) {
                    TextWithIconBulletPoint(icon = Icons.Filled.Memory, "Chip: ${pet.chipNumber}")
                }
            }
        }
    }
}

@Composable
fun TextWithIconBulletPoint(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            icon,
            text,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(4.dp),
        )
        Spacer(size = 12.dp)
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun PetScreenPreview() {
    RoarTheme {
        SurfaceScaffold {
            PetContainer(
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
                ),
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
        }
    }
}