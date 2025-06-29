package com.gaoyun.roar.ui.features.add_pet.avatar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.gaoyun.roar.config.PetsConfig
import com.gaoyun.roar.ui.common.composables.SurfaceScaffold
import com.gaoyun.roar.ui.common.composables.platformStyleClickable
import com.gaoyun.roar.ui.common.ext.getDrawableByName
import com.gaoyun.roar.ui.navigation.BackNavigationEffect
import com.gaoyun.roar.ui.navigation.NavigationSideEffect
import com.gaoyun.roar.ui.theme.RoarTheme
import com.gaoyun.roar.ui.theme.RoarThemePreview
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.choose_avatar

@Composable
fun AddPetAvatarDestination(
    navigate: (NavigationSideEffect) -> Unit,
    petType: String,
    petId: String? = null
) {
    val viewModel = koinViewModel<AddPetAvatarScreenViewModel>()

    LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {
        viewModel.petTypeChosen(petType, petId)
    }


    SurfaceScaffold(
        backHandler = { navigate(BackNavigationEffect) }
    ) {
        val viewState = viewModel.viewState.collectAsState().value
        PetAvatarScreen(
            avatars = viewState.avatars,
            petType = petType,
            onAvatarChosen = { petType, iconRes ->
                viewModel.savePetAvatar(viewState.petId ?: "", iconRes)
                navigate(ToPetData(avatar = iconRes, petType = petType))
            }
        )
    }
}

@Composable
fun PetAvatarScreen(
    avatars: List<PetsConfig.PetAvatarConfig>,
    petType: String,
    onAvatarChosen: (String, String) -> Unit
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
                text = stringResource(resource = Res.string.choose_avatar),
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 16.dp),
            )
        }
        items(avatars, key = { it.iconRes }) {
            AvatarItem(avatar = it, onAvatarChosen = onAvatarChosen, petType = petType)
        }
        item(span = titleSpan) {
            Box(
                modifier = Modifier.size(
                    WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                )
            )
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun LazyGridItemScope.AvatarItem(
    avatar: PetsConfig.PetAvatarConfig,
    petType: String,
    onAvatarChosen: (String, String) -> Unit
) {
    Surface(
        shadowElevation = 2.dp,
        tonalElevation = RoarTheme.CONTENT_CARD_ELEVATION,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .animateItem()
            .padding(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .platformStyleClickable {
                    onAvatarChosen(petType, avatar.iconRes)
                }
        ) {
            Image(
                painter = painterResource(getDrawableByName(avatar.iconRes)),
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
        PetAvatarScreen(PetsConfig.petAvatars("cat"), "cat") { _, _ -> }
    }
}