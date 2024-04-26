package com.gaoyun.roar.ui.features.pet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.ui.common.composables.RoarIcon
import com.gaoyun.roar.ui.common.composables.Spacer
import com.gaoyun.roar.ui.common.icon
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.cd_reminder
import roar.sharedlib.generated.resources.n_occurrences

@OptIn(ExperimentalResourceApi::class)
@Composable
fun InactiveInteractionCard(
    interaction: InteractionWithReminders,
    elevation: Dp,
    shape: Shape,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    shadowElevation: Dp = 0.dp,
) {
//    val lastReminderDate = interaction.reminders.maxOfOrNull { it.dateTime }?.date?.toJavaLocalDate()?.let {
//        if (it.year != SharedDateUtils.currentYear()) {
//            it.format(DateUtils.ddMmmmYyyyDateFormatter)
//        } else {
//            it.format(DateUtils.ddMmmmDateFormatter)
//        }
//    } ?: "..." TODO: fix date
    val lastReminderDate = ""

    Surface(
        tonalElevation = elevation,
        shadowElevation = shadowElevation,
        shape = shape,
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onClick(interaction.id) }
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            RoarIcon(
                icon = interaction.type.icon(),
                contentDescription = stringResource(Res.string.cd_reminder),
                modifier = Modifier
                    .padding(4.dp)
                    .size(32.dp)
            )
            Spacer(size = 8.dp)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    interaction.name,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Normal),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    stringResource(resource = Res.string.n_occurrences, interaction.reminders.size, lastReminderDate),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}