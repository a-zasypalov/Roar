package com.gaoyun.roar.ui.features.pet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.model.domain.PetWithInteractions
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.model.domain.withoutInteractions
import com.gaoyun.roar.ui.common.composables.Spacer
import com.gaoyun.roar.ui.common.ext.toLocalizedStringId
import com.gaoyun.roar.ui.theme.RoarTheme
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.delete_pet
import roar.sharedlib.generated.resources.edit_pet
import roar.sharedlib.generated.resources.inactive_reminders_title
import roar.sharedlib.generated.resources.reminders

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PetContainer(
    pet: PetWithInteractions,
    inactiveInteractions: List<InteractionWithReminders>,
    onInteractionClick: (String) -> Unit,
    onDeletePetClick: () -> Unit,
    onEditPetClick: (String) -> Unit,
    onInteractionCheckClicked: (String, Boolean, LocalDateTime) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            PetHeader(
                pet = pet.withoutInteractions(), modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 32.dp)
            )

            if (pet.interactions.isNotEmpty()) {
                Text(
                    text = stringResource(resource = Res.string.reminders),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp, start = 8.dp, end = 8.dp)
                )
            }
        }

        pet.interactions.map { it ->
            item {
                Text(
                    text = stringResource(resource = it.key.toLocalizedStringId()),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItemPlacement()
                        .padding(vertical = 8.dp, horizontal = 8.dp)
                )
            }

            items(it.value, key = { it.id }) { interaction ->
                InteractionCard(
                    interaction = interaction,
                    elevation = RoarTheme.INTERACTION_CARD_ELEVATION,
                    shadowElevation = 2.dp,
                    shape = MaterialTheme.shapes.large,
                    onClick = onInteractionClick,
                    onInteractionCheckClicked = onInteractionCheckClicked,
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .animateItemPlacement()
                )
            }
        }

        item {
            Spacer(size = 32.dp)
        }

        if (inactiveInteractions.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(resource = Res.string.inactive_reminders_title),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItemPlacement()
                        .padding(top = 4.dp, bottom = 4.dp, start = 8.dp, end = 8.dp)
                )
            }
        }

        items(inactiveInteractions, key = { it.id }) { interaction ->
            InactiveInteractionCard(
                interaction = interaction,
                elevation = RoarTheme.INACTIVE_INTERACTION_CARD_ELEVATION,
                shadowElevation = 0.dp,
                shape = MaterialTheme.shapes.large,
                onClick = onInteractionClick,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .animateItemPlacement()
                    .fillMaxWidth()
            )
        }

        item {
            Spacer(size = 32.dp)
        }

        item {
            TextButton(onClick = { onEditPetClick(pet.id) }) {
                Text(
                    text = stringResource(resource = Res.string.edit_pet),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
                )
            }

            Spacer(8.dp)

            TextButton(onClick = { onDeletePetClick() }) {
                Text(
                    text = stringResource(resource = Res.string.delete_pet),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
                )
            }

            Spacer(size = 132.dp)
        }
    }
}