package com.gaoyun.roar.ui.features.pet

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.model.domain.PetWithInteractions
import com.gaoyun.roar.ui.Spacer
import com.gaoyun.roar.ui.common.ageText
import com.gaoyun.roar.ui.theme.RoarTheme
import com.gaoyun.roar.util.SharedDateUtils
import kotlinx.datetime.LocalDateTime

@Composable
fun PetCard(
    pet: PetWithInteractions,
    modifier: Modifier = Modifier,
    onPetCardClick: (petId: String) -> Unit,
    onInteractionClick: (String) -> Unit,
    onInteractionCheckClicked: (String, Boolean, LocalDateTime) -> Unit,
) {

    Surface(
        shape = MaterialTheme.shapes.large,
        tonalElevation = RoarTheme.PET_CARD_ELEVATION,
        shadowElevation = 2.dp,
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Column(modifier = Modifier.clickable { onPetCardClick(pet.id) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
//                Image(
//                    painter = painterResource(id = context.getDrawableByName(pet.avatar)),
//                    contentDescription = pet.petType.toString(),
//                    modifier = Modifier
//                        .size(72.dp)
//                        .padding(8.dp)
//                )

                Spacer(modifier = Modifier.size(8.dp))

                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = pet.name,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = pet.ageText(),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            pet.interactions.values.flatten().sortedBy {
                it.reminders.minOfOrNull { r -> r.dateTime } ?: SharedDateUtils.MAX_DATE
            }.map { interaction ->
                InteractionCard(
                    interaction = interaction,
                    elevation = 64.dp,
                    shape = MaterialTheme.shapes.medium,
                    onClick = onInteractionClick,
                    onInteractionCheckClicked = onInteractionCheckClicked,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        .fillMaxWidth()
                        .animateContentSize()
                )
            }
            Spacer(size = 0.dp)
        }
    }
}