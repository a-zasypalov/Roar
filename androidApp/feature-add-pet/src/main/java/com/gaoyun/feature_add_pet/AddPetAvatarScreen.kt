package com.gaoyun.feature_add_pet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.gaoyun.common.NavigationKeys
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.theme.RoarTheme
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
import com.gaoyun.common.R as CommonR

@Composable
fun AddPetAvatarDestination(navHostController: NavHostController, petType: String, petId: String? = null) {
    val viewModel: AddPetAvatarScreenViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value

    AddPetAvatarScreen(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is AddPetAvatarScreenContract.Effect.Navigation.ToPetData ->
                    navHostController.navigate("${NavigationKeys.Route.ADD_PET_ROUTE}/$petType/${navigationEffect.avatar}")
                is AddPetAvatarScreenContract.Effect.Navigation.NavigateBack ->
                    navHostController.navigateUp()
            }
        },
        petType = petType
    )

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_CREATE) {
            viewModel.petTypeChosen(petType, petId)
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
                is AddPetAvatarScreenContract.Effect.Navigation -> onNavigationRequested(effect)
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
            Box(modifier = Modifier.size(WindowInsets.statusBars.asPaddingValues().calculateTopPadding()))
        }
        item(span = titleSpan) {
            Text(
                text = stringResource(id = CommonR.string.choose_avatar),
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(start = 8.dp, top = 32.dp, bottom = 16.dp),
            )
        }
        items(petAvatars, key = { it.iconRes }) { avatar ->
            Surface(
                tonalElevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
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