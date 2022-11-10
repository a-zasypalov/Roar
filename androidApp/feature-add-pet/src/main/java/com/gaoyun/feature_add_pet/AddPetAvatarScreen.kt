package com.gaoyun.feature_add_pet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.*
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
import com.gaoyun.common.ui.ButtonCard
import com.gaoyun.common.ui.SurfaceScaffold
import com.gaoyun.common.ui.getDrawableByName
import com.gaoyun.roar.config.PetsConfig
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.add_pet.avatar.AddPetAvatarScreenContract
import com.gaoyun.roar.presentation.add_pet.avatar.AddPetAvatarScreenViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.getViewModel

@Composable
fun AddPetAvatarDestination(navHostController: NavHostController, petType: String) {
    val viewModel: AddPetAvatarScreenViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value

    AddPetAvatarScreen(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is AddPetAvatarScreenContract.Effect.Navigation.ToPetData ->
                    navHostController.navigate("${NavigationKeys.RouteGlobal.ADD_PET_ROUTE}/$petType/${navigationEffect.avatar}")

                is AddPetAvatarScreenContract.Effect.Navigation.NavigateBack ->
                    navHostController.navigateUp()
            }
        },
        petType = petType
    )

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            viewModel.setEvent(AddPetAvatarScreenContract.Event.PetTypeChosen(petType))
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddPetAvatarScreen(
    state: AddPetAvatarScreenContract.State,
    effectFlow: Flow<AddPetAvatarScreenContract.Effect>,
    onEventSent: (event: AddPetAvatarScreenContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: AddPetAvatarScreenContract.Effect.Navigation) -> Unit,
    petType: String
) {
    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow.onEach { effect ->
            when (effect) {
                is AddPetAvatarScreenContract.Effect.AvatarChosen -> {
                    onNavigationRequested(AddPetAvatarScreenContract.Effect.Navigation.ToPetData(effect.avatar))
                }
                else -> {}
            }
        }.collect()
    }


    SurfaceScaffold {
        ChooseAvatar(
            petAvatars = state.avatars,
            petType = petType,
            onAvatarChosen = { avatar -> onEventSent(AddPetAvatarScreenContract.Event.AvatarChosen(avatar)) },
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ChooseAvatar(
    petAvatars: List<PetsConfig.PetAvatarConfig>,
    petType: String,
    onAvatarChosen: (String) -> Unit
) {
    val context = LocalContext.current
    val titleSpan: (LazyGridItemSpanScope) -> GridItemSpan = { GridItemSpan(3) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxSize(),
        userScrollEnabled = true
    ) {
        item(span = titleSpan) {
            Text(
                text = "Choose avatar",
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(start = 8.dp, top = 32.dp, bottom = 16.dp),
            )
        }
        items(petAvatars, key = { it.iconRes }) { avatar ->
            ButtonCard(
                modifier = Modifier
                    .animateItemPlacement()
                    .padding(8.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onAvatarChosen(avatar.iconRes) }
                ) {
                    Image(
                        painter = painterResource(id = context.getDrawableByName(avatar.iconRes)),
                        contentDescription = petType,
                        modifier = Modifier
                            .size(96.dp)
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun AddPetAvatarScreenPreview() {
    RoarTheme {
        ChooseAvatar(PetsConfig.petAvatars("cat"), "cat") {}
    }
}