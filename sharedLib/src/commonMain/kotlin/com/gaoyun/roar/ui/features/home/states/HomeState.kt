package com.gaoyun.roar.ui.features.home.states

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.roar.model.domain.PetWithInteractions
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.presentation.home_screen.HomeScreenContract
import com.gaoyun.roar.ui.common.composables.AutoResizeText
import com.gaoyun.roar.ui.common.composables.FontSizeRange
import com.gaoyun.roar.ui.common.composables.Spacer
import com.gaoyun.roar.ui.features.home.view.UserHomeHeader
import com.gaoyun.roar.ui.features.pet.PetCard
import com.gaoyun.roar.ui.features.pet.PetContainer
import com.gaoyun.roar.ui.theme.RoarThemePreview
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.your_pets


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeState(
    screenModeFull: Boolean,
    pets: List<PetWithInteractions>,
    inactiveInteractions: List<InteractionWithReminders>,
    onAddPetButtonClick: () -> Unit,
    onPetCardClick: (petId: String) -> Unit,
    onInteractionClick: (HomeScreenContract.Event.InteractionClicked) -> Unit,
    onDeletePetClick: (HomeScreenContract.Event.OnDeletePetClicked) -> Unit,
    onEditPetClick: (HomeScreenContract.Event.ToEditPetClicked) -> Unit,
    onInteractionCheckClicked: (pet: PetWithInteractions, interactionId: String, isChecked: Boolean, completionDateTime: LocalDateTime) -> Unit,
    onUserDetailsClick: () -> Unit,
    state: LazyListState,
) {

    if (pets.size == 1 && screenModeFull) {
        val pet = pets.first()
        Column {
            Box(
                modifier = Modifier.size(
                    WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                )
            )

            UserHomeHeader(
                onAddPetButtonClick = onAddPetButtonClick,
                onUserButtonButtonClick = onUserDetailsClick,
            )

            Spacer(size = 8.dp)

            PetContainer(
                pet = pet,
                inactiveInteractions = inactiveInteractions,
                onInteractionClick = { interactionId ->
                    onInteractionClick(
                        HomeScreenContract.Event.InteractionClicked(
                            petId = pet.id,
                            interactionId = interactionId
                        )
                    )
                },
                onDeletePetClick = {
                    onDeletePetClick(
                        HomeScreenContract.Event.OnDeletePetClicked(
                            pet = pet
                        )
                    )
                },
                onEditPetClick = { onEditPetClick(HomeScreenContract.Event.ToEditPetClicked(pet = pet)) },
                onInteractionCheckClicked = { interactionId, isChecked, completionDateTime ->
                    onInteractionCheckClicked(
                        pet,
                        interactionId,
                        isChecked,
                        completionDateTime
                    )
                },
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .fillMaxWidth()
            )
        }
    } else {
        LazyColumn(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            item {
                Box(
                    modifier = Modifier.size(
                        WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                    )
                )
            }
            item {
                UserHomeHeader(
                    onAddPetButtonClick = onAddPetButtonClick,
                    onUserButtonButtonClick = onUserDetailsClick,
                )
            }

            item {
                Spacer(size = 8.dp)
            }
            if (pets.size > 1) {
                item {
                    AutoResizeText(
                        text = stringResource(resource = Res.string.your_pets),
                        maxLines = 1,
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSizeRange = FontSizeRange(
                            min = 20.sp,
                            max = MaterialTheme.typography.displayMedium.fontSize,
                        ),
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                    )
                }
            }

            items(pets, key = { it.id }) { pet ->
                PetCard(
                    pet = pet,
                    onPetCardClick = onPetCardClick,
                    onInteractionClick = { interactionId ->
                        onInteractionClick(
                            HomeScreenContract.Event.InteractionClicked(
                                petId = pet.id,
                                interactionId = interactionId
                            )
                        )
                    },
                    onInteractionCheckClicked = { interactionId, isChecked, completionDateTime ->
                        onInteractionCheckClicked(
                            pet,
                            interactionId,
                            isChecked,
                            completionDateTime
                        )
                    },
                    modifier = Modifier.animateItemPlacement()
                )
            }

            item { Spacer(size = 132.dp) }
        }
    }
}

@Preview
@Composable
fun HomeStatePreview() {
    RoarThemePreview {
        HomeState(
            true,
            listOf(PetWithInteractions.preview()),
            listOf(),
            {},
            {},
            { _ -> },
            {},
            {},
            { _, _, _, _ -> },
            {},
            rememberLazyListState()
        )
    }
}