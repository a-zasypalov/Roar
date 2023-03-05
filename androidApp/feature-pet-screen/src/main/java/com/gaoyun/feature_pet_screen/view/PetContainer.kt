package com.gaoyun.feature_pet_screen.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gaoyun.common.R
import com.gaoyun.common.ext.toLocalizedStringId
import com.gaoyun.common.ui.Spacer
import com.gaoyun.roar.model.domain.PetWithInteractions
import com.gaoyun.roar.model.domain.withoutInteractions
import java.time.LocalDateTime

@Composable
fun PetContainer(
    pet: PetWithInteractions,
    showLastReminder: Boolean,
    onInteractionClick: (String) -> Unit,
    onDeletePetClick: () -> Unit,
    onEditPetClick: (String) -> Unit,
    onInteractionCheckClicked: (String, Boolean, LocalDateTime) -> Unit,
    modifier: Modifier = Modifier,
    isPartOfAnotherScreen: Boolean = false
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isPartOfAnotherScreen) {
            Box(modifier = Modifier.size(WindowInsets.statusBars.asPaddingValues().calculateTopPadding()))
        }

        PetHeader(
            pet = pet.withoutInteractions(), modifier = Modifier
                .fillMaxWidth()
                .padding(top = if (isPartOfAnotherScreen) 8.dp else 32.dp, bottom = 8.dp)
        )

        if (pet.interactions.isNotEmpty()) {
            Text(
                text = stringResource(id = R.string.reminders),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 8.dp)
            )
        }

        pet.interactions.groupBy { it.group }.toSortedMap().map {
            Text(
                text = stringResource(id = it.key.toLocalizedStringId()),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 8.dp)
            )

            it.value.sortedBy { v -> v.type }.map { interaction ->
                InteractionCard(
                    interaction = interaction,
                    showLastReminder = showLastReminder,
                    elevation = 8.dp,
                    shape = MaterialTheme.shapes.large,
                    onClick = onInteractionClick,
                    onInteractionCheckClicked = onInteractionCheckClicked,
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .fillMaxWidth()
                )
            }
        }

        Spacer(size = 32.dp)

        TextButton(onClick = { onEditPetClick(pet.id) }) {
            Text(
                text = stringResource(id = R.string.edit_pet),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
            )
        }

        Spacer(8.dp)

        TextButton(onClick = { onDeletePetClick() }) {
            Text(
                text = stringResource(id = R.string.delete_pet),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
            )
        }

        Spacer(size = 132.dp)
    }
}