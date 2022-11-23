package com.gaoyun.feature_add_pet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.gaoyun.roar.presentation.add_pet.setup.AddPetSetupScreenContract
import com.gaoyun.roar.presentation.add_pet.setup.AddPetSetupScreenViewModel
import com.gaoyun.roar.util.randomUUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.LocalDate
import org.koin.androidx.compose.getViewModel

@Composable
fun AddPetSetupDestination(navHostController: NavHostController, petId: String) {
    val viewModel: AddPetSetupScreenViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value

    AddPetSetupScreen(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is AddPetSetupScreenContract.Effect.Navigation.AddingComplete ->
                    navHostController.popBackStack(NavigationKeys.Route.ADD_PET_ROUTE, true)

                is AddPetSetupScreenContract.Effect.Navigation.NavigateBack ->
                    navHostController.navigateUp()
            }
        },
    )

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            viewModel.setEvent(AddPetSetupScreenContract.Event.PetInit(petId))
        }
    }

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddPetSetupScreen(
    state: AddPetSetupScreenContract.State,
    effectFlow: Flow<AddPetSetupScreenContract.Effect>,
    onEventSent: (event: AddPetSetupScreenContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: AddPetSetupScreenContract.Effect.Navigation) -> Unit,
) {
    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow.onEach { effect ->
            when (effect) {
                is AddPetSetupScreenContract.Effect.Continue -> {
                    onNavigationRequested(AddPetSetupScreenContract.Effect.Navigation.AddingComplete)
                }
                else -> {}
            }
        }.collect()
    }

    SurfaceScaffold {
        state.pet?.let {
            PetAddingComplete(it) { onEventSent(AddPetSetupScreenContract.Event.ContinueButtonClicked) }
        } ?: Loader(isLoading = state.isLoading)
    }
}

@Composable
private fun PetAddingComplete(
    pet: Pet,
    onContinueButtonClicked: () -> Unit
) {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = context.getDrawableByName(pet.avatar)),
            contentDescription = pet.name,
            modifier = Modifier.size(96.dp)
        )

        Spacer(16.dp)

        Text("New pet added!", style = MaterialTheme.typography.displayMedium)

        Spacer(16.dp)

        PrimaryElevatedButton(text = "Continue", onClick = onContinueButtonClicked)
    }

}

@Composable
@Preview
fun AddPetSetupScreenPreview() {
    RoarTheme {
        PetAddingComplete(
            Pet(
                randomUUID(),
                PetType.CAT,
                "British",
                "Cat",
                "ic_cat_1",
                "1",
                LocalDate.fromEpochDays(1),
                false,
                Gender.MALE,
                "",
                LocalDate.fromEpochDays(1)
            )
        ) {}
    }
}