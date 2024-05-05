package com.gaoyun.roar.ui.features.create_reminder.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfig
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfig.Companion.REPEATS_EVERY_PERIOD_ON_LAST
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfig.Companion.REPEATS_EVERY_PERIOD_ON_SAME
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfigEach
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfigEach.Companion.MONTH_STRING
import com.gaoyun.roar.model.domain.interactions.toInteractionRepeatConfigEach
import com.gaoyun.roar.ui.common.composables.DropdownMenu
import com.gaoyun.roar.ui.common.composables.ReadonlyTextField
import com.gaoyun.roar.ui.common.composables.Spacer
import com.gaoyun.roar.ui.common.composables.SurfaceCard
import com.gaoyun.roar.ui.common.composables.TextFormField
import com.gaoyun.roar.ui.common.dialog.DatePicker
import com.gaoyun.roar.ui.common.ext.toLocalizedStringId
import com.gaoyun.roar.util.DateFormats
import com.gaoyun.roar.util.formatDate
import com.gaoyun.roar.util.toLocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.isoDayNumber
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.after_three_dots
import roar.sharedlib.generated.resources.cancel
import roar.sharedlib.generated.resources.dash
import roar.sharedlib.generated.resources.day
import roar.sharedlib.generated.resources.done
import roar.sharedlib.generated.resources.end_on_date
import roar.sharedlib.generated.resources.ends
import roar.sharedlib.generated.resources.last_day
import roar.sharedlib.generated.resources.never
import roar.sharedlib.generated.resources.occurrences
import roar.sharedlib.generated.resources.on_date
import roar.sharedlib.generated.resources.reminder_ends_on
import roar.sharedlib.generated.resources.repeats_every
import roar.sharedlib.generated.resources.same_day
import roar.sharedlib.generated.resources.week

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun RepeatConfigDialog(
    repeatConfig: InteractionRepeatConfig?,
    setShowDialog: (Boolean) -> Unit,
    onConfigSave: (String) -> Unit
) {
    val repeatsEveryNumber = rememberSaveable { mutableStateOf(repeatConfig?.repeatsEveryNumber?.toString() ?: "1") }
    val repeatsEveryPeriod = rememberSaveable { mutableStateOf(repeatConfig?.repeatsEveryPeriod?.toString() ?: "month") }
    val repeatsEveryPeriodsList = InteractionRepeatConfigEach.LIST

    val weekString = stringResource(Res.string.week)
    val neverString = stringResource(Res.string.never)
    val onDateString = stringResource(Res.string.on_date)
    val afterThreeDotsString = stringResource(Res.string.after_three_dots)
    val dashString = stringResource(Res.string.dash)
    val sameDayString = stringResource(Res.string.same_day)
    val lastDayString = stringResource(Res.string.last_day)
    val repeatConfigMonthlyString = "${stringResource(Res.string.day).replaceFirstChar { it.uppercase() }} ${repeatConfig?.repeatsEveryPeriodOn ?: 1}"
    val repeatConfigString = "${stringResource(Res.string.day).replaceFirstChar { it.uppercase() }} 1"
    val repeatsEveryPeriodOnMonthDay = rememberSaveable {
        mutableStateOf(
            if (repeatConfig?.repeatsEveryPeriod.toString() == MONTH_STRING) {
                when (repeatConfig?.repeatsEveryPeriodOn) {
                    REPEATS_EVERY_PERIOD_ON_SAME -> sameDayString
                    REPEATS_EVERY_PERIOD_ON_LAST -> lastDayString
                    else -> repeatConfigMonthlyString
                }
            } else {
                repeatConfigString
            }
        )
    }

    val repeatsEveryPeriodOnWeek = remember {
        mutableStateOf(
            if (repeatConfig?.repeatsEveryPeriod.toString() == weekString) {
                val weekConfig = repeatConfig?.repeatsEveryPeriodOn?.split(",") ?: listOf()
                mapOf(
                    DayOfWeek.MONDAY to weekConfig.contains("1"),
                    DayOfWeek.TUESDAY to weekConfig.contains("2"),
                    DayOfWeek.WEDNESDAY to weekConfig.contains("3"),
                    DayOfWeek.THURSDAY to weekConfig.contains("4"),
                    DayOfWeek.FRIDAY to weekConfig.contains("5"),
                    DayOfWeek.SATURDAY to weekConfig.contains("6"),
                    DayOfWeek.SUNDAY to weekConfig.contains("7"),
                )
            } else {
                mapOf(
                    DayOfWeek.MONDAY to false,
                    DayOfWeek.TUESDAY to false,
                    DayOfWeek.WEDNESDAY to false,
                    DayOfWeek.THURSDAY to false,
                    DayOfWeek.FRIDAY to false,
                    DayOfWeek.SATURDAY to false,
                    DayOfWeek.SUNDAY to false,
                )
            }
        )
    }

    val endSafe = repeatConfig?.ends?.split(".") ?: listOf()
    val endConditionState = rememberSaveable {
        mutableStateOf(
            if (endSafe.size == 2) {
                when (endSafe[0]) {
                    "date" -> onDateString
                    "times" -> afterThreeDotsString
                    else -> neverString
                }
            } else {
                neverString
            }
        )
    }
    val endConditionStatesList = listOf(neverString, onDateString, afterThreeDotsString)

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
                Instant.fromEpochMilliseconds(endsOnDateState.value).toLocalDate().formatDate(DateFormats.ddMmmYyyyDateFormat)
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

    val showDatePickerDialog = remember { mutableStateOf(false) }
    if (showDatePickerDialog.value) {
        DatePicker.pickDate(
            title = stringResource(Res.string.reminder_ends_on),
            start = Clock.System.now(),
            selectedDateMillis = Clock.System.now().toEpochMilliseconds(),
            onDatePicked = {
                endsOnDateState.value = it
                endsOnDateStateString.value = TextFieldValue(
                    Instant.fromEpochMilliseconds(it)
                        .toLocalDate()
                        .formatDate(DateFormats.ddMmmYyyyDateFormat)
                )
                showDatePickerDialog.value = false
            },
            onDismiss = { showDatePickerDialog.value = false }
        )
    }

    Dialog(
        onDismissRequest = { setShowDialog(false) },
        properties = DialogProperties(
            dismissOnClickOutside = false
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            SurfaceCard(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = stringResource(resource = Res.string.repeats_every),
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
                            valueDisplayList = repeatsEveryPeriodsList.map { it.toInteractionRepeatConfigEach().toLocalizedStringId() },
                            listDisplayState = repeatsEveryPeriod.value.toInteractionRepeatConfigEach().toLocalizedStringId(),
                            modifier = Modifier.fillMaxWidth(1f)
                        )
                    }

                    Spacer(size = 8.dp)

                    when (repeatsEveryPeriod.value) {
                        InteractionRepeatConfigEach.WEEK_STRING -> {
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
                                                val newMap = mutableMapOf<DayOfWeek, Boolean>()
                                                repeatsEveryPeriodOnWeek.value.forEach { map ->
                                                    val newValue = if (map.key == it.key) !map.value else map.value
                                                    newMap[map.key] = newValue
                                                }
                                                repeatsEveryPeriodOnWeek.value = newMap
                                            },
                                            label = {
                                                Text(
                                                    it.key.name.take(3),
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

                        MONTH_STRING -> {
                            val days = arrayListOf<String>().apply {
                                add(stringResource(resource = Res.string.same_day))
                                for (i in 1..31) {
                                    add("${stringResource(resource = Res.string.day).replaceFirstChar { it.uppercase() }} $i")
                                }
                                add(stringResource(resource = Res.string.last_day))
                            }
                            DropdownMenu(
                                valueList = days,
                                listState = repeatsEveryPeriodOnMonthDay,
                                valueDisplayList = null,
                                listDisplayState = null,
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
                        valueDisplayList = null,
                        listDisplayState = null,
                        label = stringResource(resource = Res.string.ends),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = defaultHorizontalPadding)
                    )

                    Spacer(size = 12.dp)

                    when (endConditionState.value) {
                        stringResource(resource = Res.string.on_date) -> {
                            ReadonlyTextField(
                                value = endsOnDateStateString.value,
                                onValueChange = { endsOnDateStateString.value = it },
                                label = { Text(text = stringResource(resource = Res.string.end_on_date)) },
                                onClick = { showDatePickerDialog.value = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = defaultHorizontalPadding)
                            )
                        }

                        stringResource(resource = Res.string.after_three_dots) -> {
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
                                    text = stringResource(resource = Res.string.occurrences),
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
                            Text(
                                text = stringResource(resource = Res.string.cancel)
                            )
                        }

                        Spacer(size = 12.dp)

                        TextButton(onClick = {
                            val repeatsEveryPeriodOn = when (repeatsEveryPeriod.value) {
                                InteractionRepeatConfigEach.WEEK_STRING -> repeatsEveryPeriodOnWeek.value.filter { it.value }.keys.map { it.isoDayNumber }
                                    .joinToString(",")

                                MONTH_STRING -> if (repeatsEveryPeriodOnMonthDay.value.lowercase().contains(lastDayString.lowercase())) {
                                    REPEATS_EVERY_PERIOD_ON_LAST
                                } else if (repeatsEveryPeriodOnMonthDay.value.lowercase().contains(sameDayString.lowercase())) {
                                    REPEATS_EVERY_PERIOD_ON_SAME
                                } else {
                                    repeatsEveryPeriodOnMonthDay.value.split(" ")[1]
                                }

                                else -> dashString
                            }
                            val ends = when (endConditionState.value) {
                                onDateString -> "date.${Instant.fromEpochMilliseconds(endsOnDateState.value).toLocalDate()}"
                                afterThreeDotsString -> "times.${endsOnTimesState.value}"
                                else -> "no.0"
                            }

                            val config = "${repeatsEveryNumber.value}_${repeatsEveryPeriod.value}_${repeatsEveryPeriodOn}_$ends"

                            onConfigSave(config)
                            setShowDialog(false)
                        }) {
                            Text(text = stringResource(resource = Res.string.done))
                        }
                    }
                }
            }
        }
    }
}