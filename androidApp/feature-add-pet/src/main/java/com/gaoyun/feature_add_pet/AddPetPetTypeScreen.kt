package com.gaoyun.feature_add_pet

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.gaoyun.common.NavigationKeys
import com.gaoyun.common.theme.RoarTheme
import com.gaoyun.common.ui.ButtonCard
import com.gaoyun.common.ui.SurfaceScaffold
import com.gaoyun.common.ui.getDrawableByName
import com.gaoyun.roar.config.PetsConfig
import com.gaoyun.roar.model.domain.PetType
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.add_pet.type.AddPetPetTypeScreenContract
import com.gaoyun.roar.presentation.add_pet.type.AddPetPetTypeScreenViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.getViewModel

@Composable
fun AddPetPetTypeDestination(navHostController: NavHostController) {
    val viewModel: AddPetPetTypeScreenViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value

    AddPetPetTypeScreen(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is AddPetPetTypeScreenContract.Effect.Navigation.ToPetAvatar ->
                    navHostController.navigate("${NavigationKeys.Route.ADD_PET_ROUTE}/${navigationEffect.petType}")

                is AddPetPetTypeScreenContract.Effect.Navigation.NavigateBack ->
                    navHostController.navigateUp()
            }
        },
    )

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddPetPetTypeScreen(
    state: AddPetPetTypeScreenContract.State,
    effectFlow: Flow<AddPetPetTypeScreenContract.Effect>,
    onEventSent: (event: AddPetPetTypeScreenContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: AddPetPetTypeScreenContract.Effect.Navigation) -> Unit,
) {
    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow.onEach { effect ->
            when (effect) {
                is AddPetPetTypeScreenContract.Effect.Navigation -> onNavigationRequested(effect)
                else -> {}
            }
        }.collect()
    }

    SurfaceScaffold {
        ChoosePetType(
            petTypes = state.petTypes,
            onPetTypeChosen = { petType -> onEventSent(AddPetPetTypeScreenContract.Event.PetTypeChosen(petType)) },
        )
    }
}

@Composable
private fun ChoosePetType(
    petTypes: List<PetsConfig.PetTypeConfig>,
    onPetTypeChosen: (PetType) -> Unit
) {
    val context = LocalContext.current

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxSize()
    ) {

        Text(
            text = "New pet",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .align(Alignment.Center),
            userScrollEnabled = false
        ) {
            items(petTypes) { type ->
                ButtonCard {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { onPetTypeChosen(type.enumType) }
                    ) {
                        Image(
                            painter = painterResource(id = context.getDrawableByName(type.iconRes)),
                            contentDescription = type.nameRes,
                            modifier = Modifier
                                .size(96.dp)
                                .padding(16.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun AddPetPetTypeScreenPreview() {
    RoarTheme {
        ChoosePetType(PetsConfig.petTypes) {}
    }
}