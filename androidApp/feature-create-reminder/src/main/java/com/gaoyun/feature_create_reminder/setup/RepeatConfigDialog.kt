package com.gaoyun.feature_create_reminder.setup

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.gaoyun.common.dialog.DatePicker
import com.gaoyun.common.ui.*
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfig
import com.gaoyun.roar.util.toLocalDate
import kotlinx.datetime.*
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RepeatConfigDialog(
    repeatConfig: InteractionRepeatConfig?,
    setShowDialog: (Boolean) -> Unit,
    onConfigSave: (String) -> Unit
) {

    val activity = LocalContext.current as AppCompatActivity
    val ddMMMYYYYDateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

    val repeatsEveryNumber = rememberSaveable { mutableStateOf(repeatConfig?.repeatsEveryNumber?.toString() ?: "1") }
    val repeatsEveryPeriod = rememberSaveable { mutableStateOf(repeatConfig?.repeatsEveryPeriod?.toString() ?: "month") }
    val repeatsEveryPeriodsList = listOf("day", "week", "month", "year")

    val repeatsEveryPeriodOnMothDay = rememberSaveable {
        mutableStateOf(
            if (repeatConfig?.repeatsEveryPeriod.toString() == "month") {
                repeatConfig?.repeatsEveryPeriodOn ?: "Day 1"
            } else {
                "Day 1"
            }
        )
    }

    val repeatsEveryPeriodOnWeek = remember {
        mutableStateOf(
            if (repeatConfig?.repeatsEveryPeriod.toString() == "week") {
                val weekConfig = repeatConfig?.repeatsEveryPeriodOn?.split(",") ?: listOf()
                mapOf(
                    "Mon" to weekConfig.contains("Mon"),
                    "Tue" to weekConfig.contains("Tue"),
                    "Wed" to weekConfig.contains("Wed"),
                    "Thu" to weekConfig.contains("Thu"),
                    "Fri" to weekConfig.contains("Fri"),
                    "Sat" to weekConfig.contains("Sat"),
                    "Sun" to weekConfig.contains("Sun"),
                )
            } else {
                mapOf(
                    "Mon" to false,
                    "Tue" to false,
                    "Wed" to false,
                    "Thu" to false,
                    "Fri" to false,
                    "Sat" to false,
                    "Sun" to false,
                )
            }
        )
    }

    val endSafe = repeatConfig?.ends?.split(".") ?: listOf()
    val endConditionState = rememberSaveable {
        mutableStateOf(
            if (endSafe.size == 2) {
                when (endSafe[0]) {
                    "date" -> "On date"
                    "times" -> "After..."
                    else -> "Never"
                }
            } else {
                "Never"
            }
        )
    }
    val endConditionStatesList = listOf("Never", "On date", "After...")

    val endsOnDateState = remember {
        mutableStateOf(
            if (endSafe.size == 2 && endSafe[0] == "date")
                LocalDate.parse(endSafe[1]).atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
            else
                Clock.System.now().toEpochMilliseconds()
        )
    }
    val endsOnDateStateString = remember {
        mutableStateOf(
            TextFieldValue(
                Instant.fromEpochMilliseconds(endsOnDateState.value).toLocalDate().toJavaLocalDate().format(ddMMMYYYYDateFormatter)
            )
        )
    }

    val endsOnTimesState = remember {
        mutableStateOf(
            if (endSafe.size == 2 && endSafe[0] == "times") {
                endSafe[1]
            } else {
                "2"
            }
        )
    }

    val defaultHorizontalPadding = 24.dp

    Dialog(
        onDismissRequest = { setShowDialog(false) },
        properties = DialogProperties(
            dismissOnClickOutside = false
        )
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxSize()
        ) {
            SurfaceCard(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 36.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Repeats every",
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.padding(horizontal = defaultHorizontalPadding)
                    )

                    Spacer(size = 24.dp)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = defaultHorizontalPadding)
                    ) {
                        TextFormField(
                            text = repeatsEveryNumber.value,
                            keyboardType = KeyboardType.Decimal,
                            onChange = { repeatsEveryNumber.value = it },
                            modifier = Modifier.fillMaxWidth(0.25f),
                            imeAction = ImeAction.Done,
                        )
                        Spacer(size = 12.dp)
                        DropdownMenu(
                            valueList = repeatsEveryPeriodsList,
                            listState = repeatsEveryPeriod,
                            modifier = Modifier.fillMaxWidth(1f)
                        )
                    }

                    Spacer(size = 8.dp)

                    when (repeatsEveryPeriod.value) {
                        "week" -> {
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                item {
                                    Spacer(size = 20.dp)
                                }
                                repeatsEveryPeriodOnWeek.value.forEach {
                                    item {
                                        FilterChip(
                                            selected = it.value,
                                            onClick = {
                                                val newMap = mutableMapOf<String, Boolean>()
                                                repeatsEveryPeriodOnWeek.value.forEach { map ->
                                                    val newValue = if (map.key == it.key) !map.value else map.value
                                                    newMap[map.key] = newValue
                                                }
                                                repeatsEveryPeriodOnWeek.value = newMap
                                            },
                                            label = {
                                                Text(
                                                    it.key,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                                )
                                            },
                                            modifier = Modifier.padding(horizontal = 4.dp)
                                        )
                                    }
                                }
                                item {
                                    Spacer(size = 20.dp)
                                }
                            }
                        }
                        "month" -> {
                            val days = arrayListOf<String>().apply {
                                for (i in 1..31) {
                                    add("Day $i")
                                }
                                add("Last Day")
                            }
                            DropdownMenu(
                                valueList = days,
                                listState = repeatsEveryPeriodOnMothDay,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = defaultHorizontalPadding)
                            )
                            Spacer(size = 8.dp)
                        }
                    }

                    Spacer(size = 8.dp)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .padding(horizontal = defaultHorizontalPadding)
                            .background(MaterialTheme.colorScheme.outline)
                    )

                    Spacer(size = 16.dp)

                    DropdownMenu(
                        valueList = endConditionStatesList,
                        listState = endConditionState,
                        label = "Ends",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = defaultHorizontalPadding)
                    )

                    Spacer(size = 12.dp)

                    when (endConditionState.value) {
                        "On date" -> {
                            ReadonlyTextField(
                                value = endsOnDateStateString.value,
                                onValueChange = { endsOnDateStateString.value = it },
                                label = { Text(text = "End on date") },
                                onClick = {
                                    DatePicker.pickDate(
                                        title = "Reminder ends on",
                                        start = Clock.System.now().toEpochMilliseconds(),
                                        fragmentManager = activity.supportFragmentManager,
                                        selectedDateMillis = Clock.System.now().toEpochMilliseconds(),
                                        onDatePicked = {
                                            endsOnDateState.value = it
                                            endsOnDateStateString.value = TextFieldValue(
                                                Instant.fromEpochMilliseconds(it)
                                                    .toLocalDate()
                                                    .toJavaLocalDate()
                                                    .format(ddMMMYYYYDateFormatter)
                                            )
                                        }
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = defaultHorizontalPadding)
                            )
                        }
                        "After..." -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = defaultHorizontalPadding)
                            ) {
                                TextFormField(
                                    text = endsOnTimesState.value,
                                    keyboardType = KeyboardType.Decimal,
                                    onChange = { endsOnTimesState.value = it },
                                    modifier = Modifier.fillMaxWidth(0.25f),
                                    imeAction = ImeAction.Done,
                                )
                                Spacer(size = 12.dp)
                                Text(
                                    text = "occurrences",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.fillMaxWidth(1f)
                                )
                            }
                        }
                    }

                    Spacer(size = 12.dp)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = defaultHorizontalPadding),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = {
                            setShowDialog(false)
                        }) {
                            Text(text = "Cancel")
                        }

                        Spacer(size = 12.dp)

                        TextButton(onClick = {
                            val repeatsEveryPeriodOn = when (repeatsEveryPeriod.value) {
                                "week" -> repeatsEveryPeriodOnWeek.value.filter { it.value }.keys.joinToString(",")
                                "month" -> repeatsEveryPeriodOnMothDay.value
                                else -> "-"
                            }
                            val ends = when (endConditionState.value) {
                                "On date" -> "date.${Instant.fromEpochMilliseconds(endsOnDateState.value).toLocalDate()}"
                                "After..." -> "times.${endsOnTimesState.value}"
                                else -> "no.0"
                            }

                            val config =
                                "${repeatsEveryNumber.value}_${repeatsEveryPeriod.value}_${repeatsEveryPeriodOn}_$ends"

                            onConfigSave(config)
                            setShowDialog(false)
                        }) {
                            Text(text = "Done")
                        }
                    }
                }
            }
        }
    }
}