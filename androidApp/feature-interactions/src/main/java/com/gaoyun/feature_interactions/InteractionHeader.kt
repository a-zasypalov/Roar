package com.gaoyun.feature_interactions

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.gaoyun.common.ui.Spacer
import com.gaoyun.common.ui.TextFormField
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.interactions.InteractionGroup
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders

@Composable
internal fun InteractionHeader(
    pet: Pet,
    interaction: InteractionWithReminders,
    notesState: MutableState<String?>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = interaction.name,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(0.8f)
            )

            Spacer(8.dp)

            Surface(
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.2f),
                shape = CircleShape,
            ) {
                Image(
                    painter = painterResource(id = com.gaoyun.common.R.drawable.ic_cat),
                    contentDescription = pet.name,
                    modifier = Modifier
                        .height(72.dp)
                        .padding(all = 12.dp)
                )
            }
        }

        Spacer(size = 32.dp)

        Card(
            elevation = CardDefaults.elevatedCardElevation(0.dp),
            shape = MaterialTheme.shapes.large,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                val groupIcon = when (interaction.group) {
                    InteractionGroup.HEALTH -> Icons.Filled.MedicalServices
                    InteractionGroup.CARE -> Icons.Filled.Spa
                    InteractionGroup.ROUTINE -> Icons.Filled.Pets
                }
                TextWithIconBulletPoint(icon = groupIcon, interaction.group.toString())

                interaction.repeatConfig?.let {
                    TextWithIconBulletPoint(icon = Icons.Filled.Repeat, it.toString())
                } ?: TextWithIconBulletPoint(icon = Icons.Filled.Repeat, "Doesn't repeat")
            }
        }

        Spacer(size = 16.dp)

        TextFormField(
            shape = MaterialTheme.shapes.large,
            leadingIcon = {
                Icon(Icons.Filled.Notes, "notes")
            },
            text = notesState.value ?: "",
            imeAction = ImeAction.Default,
            onChange = { notesState.value = it }
        )
    }
}

@Composable
fun TextWithIconBulletPoint(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            icon,
            text,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(4.dp),
        )
        Spacer(size = 12.dp)
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}