package com.gaoyun.feature_pet_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.common.ui.LabelledCheckBox
import com.gaoyun.common.ui.Spacer
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun InteractionCard(
    interaction: InteractionWithReminders,
    onClick: (String) -> Unit
) {
    Surface(
        tonalElevation = 4.dp,
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
    ) {
        Column(modifier = Modifier
            .clickable { onClick(interaction.id)}
            .padding(top = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Icon(
                    Icons.Filled.Alarm,
                    "",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(4.dp)
                )
                Spacer(size = 12.dp)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        interaction.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    val repeatText = interaction.repeatConfig?.toString() ?: "No repeat"
                    Text(
                        repeatText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(size = 8.dp)

            interaction.reminders
                .filter { !it.isCompleted || it.dateTime > Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) }
                .maxByOrNull { it.dateTime }?.let { nextReminder ->
                    LabelledCheckBox(
                        checked = !nextReminder.isCompleted,
                        label = "Next: ${nextReminder.dateTime.date} at 09:00",
                        modifier = Modifier.fillMaxWidth(),
                        verticalPadding = 16.dp,
                        horizontalPadding = 20.dp,
                        spacerSize = 14.dp,
                        onCheckedChange = {}
                    )
                } ?: Text(
                text = "No active reminder",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
    }
}