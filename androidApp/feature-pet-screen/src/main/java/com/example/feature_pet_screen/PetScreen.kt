package com.example.feature_pet_screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.gaoyun.common.NavigationKeys
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.theme.RoarTheme
import com.gaoyun.common.ui.*
import com.gaoyun.roar.model.domain.Gender
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.PetType
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.pet_screen.PetScreenContract
import com.gaoyun.roar.presentation.pet_screen.PetScreenViewModel
import com.gaoyun.roar.util.toLocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.Clock
import org.koin.androidx.compose.getViewModel

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
                is PetScreenContract.Effect.Navigation.ToInteractionTemplates -> navHostController.navigate("${NavigationKeys.RouteGlobal.ADD_REMINDER}/${navigationEffect.petId}")
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
                onClick = {

                })
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Box {
            state.pet?.let { pet ->
                PetContainer(pet = pet)
            }

            Loader(isLoading = state.isLoading)
        }
    }
}

@Composable
private fun PetContainer(pet: Pet) {
    Column(
        modifier = Modifier
            .padding(top = 32.dp, start = 16.dp, end = 16.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        PetHeader(pet = pet, modifier = Modifier.fillMaxWidth())
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
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 8.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = CircleShape,
        ) {
            Image(
                painter = painterResource(id = context.getDrawableByName(pet.avatar)),
                contentDescription = pet.name,
                modifier = Modifier
                    .height(96.dp)
                    .padding(all = 16.dp)
            )
        }

        Text(
            text = pet.name,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayMedium
        )

        Spacer(size = 16.dp)

        Surface(
            shape = MaterialTheme.shapes.large,
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                when(pet.gender) {
                    Gender.MALE -> TextWithIconBulletPoint(icon = Icons.Filled.Male, "Male, $isSterilized")
                    Gender.FEMALE -> TextWithIconBulletPoint(icon = Icons.Filled.Female, "Female, $isSterilized")
                }

                TextWithIconBulletPoint(icon = Icons.Filled.Cake, pet.birthday.toString())
                TextWithIconBulletPoint(icon = Icons.Filled.Pets, pet.breed)

                if(pet.chipNumber.isNotEmpty()) {
                    TextWithIconBulletPoint(icon = Icons.Filled.Memory, "Chip: ${pet.chipNumber}")
                }
            }
        }

        Spacer(size = 16.dp)
    }
}

@Composable
fun TextWithIconBulletPoint(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Icon(
            icon,
            text,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(8.dp)
        )
        Spacer(size = 8.dp)
        Text(text = text, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
@Preview
fun PetScreenPreview() {
    RoarTheme {
        Surface {
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
                )
            )
        }
    }
}