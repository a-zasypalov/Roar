package com.gaoyun.feature_interactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.common.R
import com.gaoyun.common.composables.AutoResizeText
import com.gaoyun.common.composables.FontSizeRange
import com.gaoyun.common.composables.RoarIcon
import com.gaoyun.common.composables.Spacer
import com.gaoyun.common.composables.TextFormField
import com.gaoyun.common.ext.remindConfigTextFull
import com.gaoyun.common.ext.repeatConfigTextFull
import com.gaoyun.common.ext.toLocalizedStringId
import com.gaoyun.common.icon
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.interactions.InteractionGroup
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.ui.theme.RoarTheme

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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AutoResizeText(
                text = interaction.name,
                maxLines = 2,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontSizeRange = FontSizeRange(
                    min = 20.sp,
                    max = MaterialTheme.typography.displayMedium.fontSize,
                ),
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(0.8f)
            )

            Spacer(8.dp)

            Surface(
                tonalElevation = RoarTheme.INFORMATION_CARD_ELEVATION,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.2f),
                shape = CircleShape,
            ) {
                RoarIcon(
                    icon = interaction.type.icon(),
                    contentDescription = pet.name,
                    modifier = Modifier
                        .height(72.dp)
                        .padding(all = 16.dp)
                )
            }
        }

        Spacer(size = 16.dp)

        Surface(
            tonalElevation = RoarTheme.INFORMATION_CARD_ELEVATION,
            shape = MaterialTheme.shapes.large,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                val groupIcon = when (interaction.group) {
                    InteractionGroup.HEALTH -> Icons.Default.MedicalServices
                    InteractionGroup.CARE -> Icons.Default.Spa
                    InteractionGroup.ROUTINE -> Icons.Default.Pets
                }
                TextWithIconBulletPoint(
                    icon = groupIcon,
                    stringResource(id = interaction.group.toLocalizedStringId())
                )

                TextWithIconBulletPoint(
                    icon = Icons.Default.Notifications,
                    text = interaction.remindConfig.remindConfigTextFull()
                )

                interaction.repeatConfig?.let {
                    TextWithIconBulletPoint(icon = Icons.Default.Repeat, it.repeatConfigTextFull())
                } ?: TextWithIconBulletPoint(
                    icon = Icons.Default.Repeat,
                    stringResource(id = R.string.doesnt_repeat)
                )

                if (interaction.isActive) {
                    TextWithIconBulletPoint(
                        icon = Icons.Default.Done,
                        stringResource(id = R.string.active)
                    )
                } else {
                    TextWithIconBulletPoint(
                        icon = Icons.Default.Close,
                        stringResource(id = R.string.not_active)
                    )
                }
            }
        }

        Spacer(size = 16.dp)

        TextFormField(
            shape = MaterialTheme.shapes.large,
            leadingIcon = {
                Icon(Icons.AutoMirrored.Filled.Notes, stringResource(id = R.string.cd_notes))
            },
            text = notesState.value ?: "",
            label = stringResource(id = R.string.notes),
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