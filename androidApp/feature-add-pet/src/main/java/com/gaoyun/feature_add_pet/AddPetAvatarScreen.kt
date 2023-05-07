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
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.composables.SurfaceScaffold
import com.gaoyun.common.ext.getDrawableByName
import com.gaoyun.common.theme.RoarThemePreview
import com.gaoyun.roar.config.PetsConfig
import com.gaoyun.roar.presentation.BackNavigationEffect
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.add_pet.avatar.AddPetAvatarScreenContract
import com.gaoyun.roar.presentation.add_pet.avatar.AddPetAvatarScreenViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.getViewModel
import com.gaoyun.common.R as CommonR

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddPetAvatarDestination(
    onNavigationCall: (NavigationSideEffect) -> Unit,
    petType: String,
    petId: String? = null
) {
    val viewModel: AddPetAvatarScreenViewModel = getViewModel()

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_CREATE) {
            viewModel.petTypeChosen(petType, petId)
        }
    }

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        viewModel.effect.onEach { effect ->
            when (effect) {
                is AddPetAvatarScreenContract.Effect.NavigateBack -> onNavigationCall(BackNavigationEffect)
                is AddPetAvatarScreenContract.Effect.Navigation -> onNavigationCall(effect)
            }
        }.collect()
    }


    SurfaceScaffold(
        backHandler = { onNavigationCall(BackNavigationEffect) }
    ) {
        PetAvatarScreen(
            avatars = viewModel.viewState.collectAsState().value.avatars,
            petType = petType,
            onAvatarChosen = viewModel::setEvent
        )
    }
}

@Composable
fun PetAvatarScreen(
    avatars: List<PetsConfig.PetAvatarConfig>,
    petType: String,
    onAvatarChosen: (AddPetAvatarScreenContract.Event.AvatarChosen) -> Unit
) {
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
                text = stringResource(id = CommonR.string.choose_avatar),
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 16.dp),
            )
        }
        items(avatars, key = { it.iconRes }) {
            AvatarItem(avatar = it, onAvatarChosen = onAvatarChosen, petType = petType)
        }
        item(span = titleSpan) {
            Box(modifier = Modifier.size(WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()))
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun LazyGridItemScope.AvatarItem(
    avatar: PetsConfig.PetAvatarConfig,
    petType: String,
    onAvatarChosen: (AddPetAvatarScreenContract.Event.AvatarChosen) -> Unit
) {
    val context = LocalContext.current
    Surface(
        tonalElevation = 12.dp,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .animateItemPlacement()
            .padding(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .clickable { onAvatarChosen(AddPetAvatarScreenContract.Event.AvatarChosen(avatar.iconRes, petType)) }
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

@Composable
@Preview
fun AddPetAvatarScreenPreview() {
    RoarThemePreview {
        PetAvatarScreen(PetsConfig.petAvatars("cat"), "cat") {}
    }
}