package com.gaoyun.feature_pet_screen.view

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gaoyun.common.DateUtils
import com.gaoyun.common.R
import com.gaoyun.common.composables.RoarIcon
import com.gaoyun.common.composables.Spacer
import com.gaoyun.common.icon
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.util.toLocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaLocalDate

@Composable
fun InactiveInteractionCard(
    interaction: InteractionWithReminders,
    elevation: Dp,
    shape: Shape,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    shadowElevation: Dp = 0.dp,
) {
    val lastReminderDate = interaction.reminders.maxOfOrNull { it.dateTime }?.date?.let {
        if (it.year != Clock.System.now().toLocalDate().year) {
            stringResource(id = R.string.last_occurrence_on, it.toJavaLocalDate().format(DateUtils.ddMmmmYyyyDateFormatter))
        } else {
            stringResource(id = R.string.last_occurrence_on, it.toJavaLocalDate().format(DateUtils.ddMmmmDateFormatter))
        }
    } ?: stringResource(id = R.string.no_active_reminder)

    Surface(
        tonalElevation = elevation,
        shadowElevation = shadowElevation,
        shape = shape,
        modifier = modifier,
    ) {
        Column(modifier = Modifier
            .clickable { onClick(interaction.id) }
            .padding(top = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                RoarIcon(
                    icon = interaction.type.icon(),
                    contentDescription = stringResource(id = R.string.cd_reminder),
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
                        stringResource(id = R.string.n_occurrences, interaction.reminders.size),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(size = 8.dp)

            Text(
                text = lastReminderDate,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
    }
}