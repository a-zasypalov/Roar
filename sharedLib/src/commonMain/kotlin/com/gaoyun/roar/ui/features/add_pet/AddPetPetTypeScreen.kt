package com.gaoyun.roar.ui.features.add_pet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.config.PetsConfig
import com.gaoyun.roar.presentation.BackNavigationEffect
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.add_pet.type.AddPetPetTypeScreenContract
import com.gaoyun.roar.presentation.add_pet.type.AddPetPetTypeScreenViewModel
import com.gaoyun.roar.ui.common.composables.SurfaceScaffold
import com.gaoyun.roar.ui.common.composables.RoarIcon
import com.gaoyun.roar.ui.theme.RoarTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.new_pet

@Composable
fun AddPetPetTypeDestination(onNavigationCall: (NavigationSideEffect) -> Unit) {
    val viewModel = koinViewModel(vmClass = AddPetPetTypeScreenViewModel::class)
    val state = viewModel.viewState.collectAsState().value

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        viewModel.effect.onEach { effect ->
            when (effect) {
                is AddPetPetTypeScreenContract.Effect.Navigation -> onNavigationCall(effect)
            }
        }.collect()
    }

    SurfaceScaffold(
        backHandler = { onNavigationCall(BackNavigationEffect) }
    ) {
        ChoosePetType(
            petTypes = state.petTypes,
            onPetTypeChosen = viewModel::setEvent,
        )
    }

}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun ChoosePetType(
    petTypes: List<PetsConfig.PetTypeConfig>,
    onPetTypeChosen: (AddPetPetTypeScreenContract.Event.PetTypeChosen) -> Unit
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxSize()
    ) {

        Text(
            text = stringResource(resource = Res.string.new_pet),
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .align(Alignment.Center),
            userScrollEnabled = false
        ) {
            items(petTypes) { type ->
                Surface(
                    shadowElevation = 2.dp,
                    tonalElevation = RoarTheme.CONTENT_CARD_ELEVATION,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                onPetTypeChosen(
                                    AddPetPetTypeScreenContract.Event.PetTypeChosen(
                                        type.enumType
                                    )
                                )
                            }
                    ) {
                        RoarIcon(
                            icon = "", //context.getDrawableByName(type.iconRes),
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

//@Composable
//@Preview
//fun AddPetPetTypeScreenPreview() {
//    RoarThemePreview {
//        ChoosePetType(PetsConfig.petTypes) {}
//    }
//}