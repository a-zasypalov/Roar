package com.gaoyun.feature_pet_screen.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.common.ui.Spacer
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders

@Composable
fun PetContainer(
    pet: Pet,
    interactions: List<InteractionWithReminders>,
    showLastReminder: Boolean,
    onInteractionClick: (String) -> Unit,
    onDeletePetClick: () -> Unit,
    onEditPetClick: (String) -> Unit,
    onInteractionCheckClicked: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isPartOfAnotherScreen: Boolean = false
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(!isPartOfAnotherScreen) {
            Box(modifier = Modifier.size(WindowInsets.statusBars.asPaddingValues().calculateTopPadding()))
        }

        PetHeader(
            pet = pet, modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 8.dp)
        )

        if (interactions.isNotEmpty()) {
            Text(
                text = "Reminders",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 8.dp)
            )
        }

        interactions.groupBy { it.group }.toSortedMap().map {
            Text(
                text = it.key.toString(),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 8.dp)
            )

            it.value.sortedBy { v -> v.type }.map { interaction ->
                InteractionCard(interaction, showLastReminder, onInteractionClick, onInteractionCheckClicked)
            }
        }

        Spacer(size = 32.dp)

        TextButton(onClick = { onEditPetClick(pet.id) }) {
            Text(
                text = "Edit pet",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
            )
        }

        Spacer(8.dp)

        TextButton(onClick = { onDeletePetClick() }) {
            Text(
                text = "Delete pet",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
            )
        }

        Spacer(size = 132.dp)
    }
}