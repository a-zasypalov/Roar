package com.gaoyun.roar.ui.features.add_pet

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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.config.PetsConfig
import com.gaoyun.roar.model.domain.PetType
import com.gaoyun.roar.presentation.add_pet.AddPetPetTypeScreenViewModel
import com.gaoyun.roar.presentation.add_pet.ToPetAvatar
import com.gaoyun.roar.ui.common.composables.RoarIcon
import com.gaoyun.roar.ui.common.composables.SurfaceScaffold
import com.gaoyun.roar.ui.common.composables.platformStyleClickable
import com.gaoyun.roar.ui.navigation.BackNavigationEffect
import com.gaoyun.roar.ui.navigation.NavigationSideEffect
import com.gaoyun.roar.ui.theme.RoarTheme
import com.gaoyun.roar.ui.theme.RoarThemePreview
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.new_pet

@Composable
fun AddPetPetTypeDestination(
    navigate: (NavigationSideEffect) -> Unit
) {
    val viewModel = koinViewModel<AddPetPetTypeScreenViewModel>()
    val viewState = viewModel.viewState.collectAsState().value

    SurfaceScaffold(
        backHandler = { navigate(BackNavigationEffect) }
    ) {
        ChoosePetType(
            petTypes = viewState.petTypes,
            onPetTypeChosen = { petType ->
                navigate(ToPetAvatar(petType.name))
            }
        )
    }
}

@Composable
private fun ChoosePetType(
    petTypes: List<PetsConfig.PetTypeConfig>,
    onPetTypeChosen: (PetType) -> Unit
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
                            .platformStyleClickable {
                                onPetTypeChosen(type.enumType)
                            }
                    ) {
                        RoarIcon(
                            icon = type.iconRes,
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
    RoarThemePreview {
        ChoosePetType(PetsConfig.petTypes) {}
    }
}