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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.gaoyun.common.DateUtils.ddMmmYyyyDateFormatter
import com.gaoyun.common.R
import com.gaoyun.common.dialog.DatePicker
import com.gaoyun.common.ext.toLocalizedStringId
import com.gaoyun.common.ui.*
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfig
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfig.Companion.REPEATS_EVERY_PERIOD_ON_LAST
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfigEach
import com.gaoyun.roar.model.domain.interactions.InteractionRepeatConfigEach.Companion.MONTH_STRING
import com.gaoyun.roar.model.domain.interactions.toInteractionRepeatConfigEach
import com.gaoyun.roar.util.toLocalDate
import kotlinx.datetime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RepeatConfigDialog(
    repeatConfig: InteractionRepeatConfig?,
    setShowDialog: (Boolean) -> Unit,
    onConfigSave: (String) -> Unit
) {

    val activity = LocalContext.current as AppCompatActivity

    val repeatsEveryNumber = rememberSaveable { mutableStateOf(repeatConfig?.repeatsEveryNumber?.toString() ?: "1") }
    val repeatsEveryPeriod = rememberSaveable { mutableStateOf(repeatConfig?.repeatsEveryPeriod?.toString() ?: activity.getString(R.string.month)) }
    val repeatsEveryPeriodsList = InteractionRepeatConfigEach.LIST

    val repeatsEveryPeriodOnMonthDay = rememberSaveable {
        mutableStateOf(
            if (repeatConfig?.repeatsEveryPeriod.toString() == MONTH_STRING) {
                "${activity.getString(R.string.day).replaceFirstChar { it.uppercase() }} ${repeatConfig?.repeatsEveryPeriodOn ?: 1}"
            } else {
                "${activity.getString(R.string.day).replaceFirstChar { it.uppercase() }} 1"
            }
        )
    }

    val repeatsEveryPeriodOnWeek = remember {
        mutableStateOf(
            if (repeatConfig?.repeatsEveryPeriod.toString() == activity.getString(R.string.week)) {
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
                    "date" -> activity.getString(R.string.on_date)
                    "times" -> activity.getString(R.string.after_three_dots)
                    else -> activity.getString(R.string.never)
                }
            } else {
                activity.getString(R.string.never)
            }
        )
    }
    val endConditionStatesList = listOf(
        activity.getString(R.string.never),
        activity.getString(R.string.on_date),
        activity.getString(R.string.after_three_dots)
    )

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
                Instant.fromEpochMilliseconds(endsOnDateState.value).toLocalDate().toJavaLocalDate().format(ddMmmYyyyDateFormatter)
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
                        text = stringResource(id = R.string.repeats_every),
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
                        InteractionRepeatConfigEach.MONTH_STRING -> {
                            val days = arrayListOf<String>().apply {
                                for (i in 1..31) {
                                    add("${stringResource(id = R.string.day).replaceFirstChar { it.uppercase() }} $i")
                                }
                                add(stringResource(id = R.string.last_day))
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
                        label = stringResource(id = R.string.ends),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = defaultHorizontalPadding)
                    )

                    Spacer(size = 12.dp)

                    when (endConditionState.value) {
                        stringResource(id = R.string.on_date) -> {
                            ReadonlyTextField(
                                value = endsOnDateStateString.value,
                                onValueChange = { endsOnDateStateString.value = it },
                                label = { Text(text = stringResource(id = R.string.end_on_date)) },
                                onClick = {
                                    DatePicker.pickDate(
                                        title = activity.getString(R.string.reminder_ends_on),
                                        start = Clock.System.now().toEpochMilliseconds(),
                                        fragmentManager = activity.supportFragmentManager,
                                        selectedDateMillis = Clock.System.now().toEpochMilliseconds(),
                                        onDatePicked = {
                                            endsOnDateState.value = it
                                            endsOnDateStateString.value = TextFieldValue(
                                                Instant.fromEpochMilliseconds(it)
                                                    .toLocalDate()
                                                    .toJavaLocalDate()
                                                    .format(ddMmmYyyyDateFormatter)
                                            )
                                        }
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = defaultHorizontalPadding)
                            )
                        }
                        stringResource(id = R.string.after_three_dots) -> {
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
                                    text = stringResource(id = R.string.occurrences),
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
                            Text(text = stringResource(id = R.string.cancel))
                        }

                        Spacer(size = 12.dp)

                        TextButton(onClick = {
                            val repeatsEveryPeriodOn = when (repeatsEveryPeriod.value) {
                                InteractionRepeatConfigEach.WEEK_STRING -> repeatsEveryPeriodOnWeek.value.filter { it.value }.keys.map { it.isoDayNumber }.joinToString(",")
                                InteractionRepeatConfigEach.MONTH_STRING ->
                                    if (repeatsEveryPeriodOnMonthDay.value.lowercase().contains(activity.getString(R.string.last_day).lowercase())) {
                                        REPEATS_EVERY_PERIOD_ON_LAST
                                    } else {
                                        repeatsEveryPeriodOnMonthDay.value.split(" ")[1]
                                    }
                                else -> activity.getString(R.string.dash)
                            }
                            val ends = when (endConditionState.value) {
                                activity.getString(R.string.on_date) -> "date.${Instant.fromEpochMilliseconds(endsOnDateState.value).toLocalDate()}"
                                activity.getString(R.string.after_three_dots) -> "times.${endsOnTimesState.value}"
                                else -> "no.0"
                            }

                            val config = "${repeatsEveryNumber.value}_${repeatsEveryPeriod.value}_${repeatsEveryPeriodOn}_$ends"

                            onConfigSave(config)
                            setShowDialog(false)
                        }) {
                            Text(text = stringResource(id = R.string.done))
                        }
                    }
                }
            }
        }
    }
}