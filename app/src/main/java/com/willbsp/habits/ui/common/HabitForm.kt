package com.willbsp.habits.ui.common

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.willbsp.habits.R
import com.willbsp.habits.data.model.HabitFrequency
import java.text.DateFormatSymbols
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun HabitForm(
    modifier: Modifier = Modifier,
    onValueChange: (HabitUiState.Habit) -> Unit,
    habitUiState: HabitUiState.Habit
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        OutlinedTextField(
            value = habitUiState.name,
            singleLine = true,
            isError = habitUiState.nameIsInvalid,
            onValueChange = { onValueChange(habitUiState.copy(name = it)) },
            label = { Text(stringResource(R.string.modify_habit_name)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth()
        )

        HabitFrequencyDropdown(
            modifier = Modifier.fillMaxWidth(),
            uiState = habitUiState,
            onValueChange = onValueChange
        )

        HabitReminderDropdown(
            modifier = Modifier.fillMaxWidth(),
            uiState = habitUiState,
            onValueChange = onValueChange
        )

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HabitReminderDropdown(
    modifier: Modifier = Modifier,
    uiState: HabitUiState.Habit,
    onValueChange: (HabitUiState.Habit) -> Unit,
) {

    val options = listOf("None", "Every day", "Specific days")
    var optionSelected: String by remember { mutableStateOf("None") }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        ExposedDropdownMenuBox(
            modifier = Modifier,
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {

            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                value = optionSelected,
                onValueChange = {},
                label = { Text("Reminder") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            optionSelected = option
                            // onValueChange
                            expanded = false
                        }
                    )
                }
            }

        }

        AnimatedVisibility(visible = (optionSelected == "Every day") || (optionSelected == "Specific days")) {

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HabitReminderTimeDropdown(
                    modifier = Modifier.fillMaxWidth(),
                    time = LocalTime.NOON,
                    onClick = {} // TODO open time picker
                )
                AnimatedVisibility(visible = (optionSelected == "Specific days")) {
                    HabitReminderDays(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }


        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HabitReminderTimeDropdown(
    modifier: Modifier = Modifier,
    time: LocalTime,
    onClick: () -> Unit
) {

    val formatter: DateTimeFormatter = DateTimeFormatter.ISO_TIME

    OutlinedTextField(
        modifier = modifier.clickable { onClick() },
        value = time.format(formatter).toString(),
        readOnly = true,
        onValueChange = {}, // TODO update viewmodel
        label = { Text("Time") },
        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) }
    )

}

@Composable
private fun HabitReminderDays(
    // TODO change week start?
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val weekdays = DateFormatSymbols.getInstance().weekdays
        for (day in 1..7) {
            SideEffect {
                Log.d("reminders", weekdays[day])
            }
            OutlinedIconToggleButton(
                checked = day == 3, onCheckedChange = {}
            ) {
                Text(weekdays[day].first().toString())
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HabitFrequencyDropdown(
    modifier: Modifier = Modifier,
    uiState: HabitUiState.Habit,
    onValueChange: (HabitUiState.Habit) -> Unit
) {

    val frequencyOptions = HabitFrequency.values()
    var frequencyExpanded by remember { mutableStateOf(false) }
    var frequencySelected by remember { mutableStateOf(uiState.frequency) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        ExposedDropdownMenuBox(
            expanded = frequencyExpanded,
            onExpandedChange = { frequencyExpanded = !frequencyExpanded },
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                value = stringResource(id = frequencySelected.userReadableStringRes),
                onValueChange = {},
                label = { Text(stringResource(R.string.modify_frequency)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = frequencyExpanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
            )
            ExposedDropdownMenu(
                expanded = frequencyExpanded,
                onDismissRequest = { frequencyExpanded = false },
            ) {
                frequencyOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(stringResource(id = selectionOption.userReadableStringRes)) },
                        onClick = {
                            frequencySelected = selectionOption
                            onValueChange(uiState.copy(frequency = selectionOption))
                            frequencyExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }

        val repeatOptions = 1..6
        var repeatExpanded by remember { mutableStateOf(false) }
        var selectedRepeat by remember { mutableStateOf(uiState.repeat) }

        AnimatedVisibility(visible = uiState.frequency == HabitFrequency.WEEKLY) {

            ExposedDropdownMenuBox(
                expanded = repeatExpanded,
                onExpandedChange = { repeatExpanded = !repeatExpanded },
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    readOnly = true,
                    value = selectedRepeat.toString(),
                    onValueChange = {},
                    label = { Text(stringResource(id = R.string.modify_times_per_week)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = repeatExpanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = repeatExpanded,
                    onDismissRequest = { repeatExpanded = false },
                ) {
                    repeatOptions.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption.toString()) },
                            onClick = {
                                selectedRepeat = selectionOption
                                onValueChange(uiState.copy(repeat = selectionOption))
                                repeatExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }

        }

    }

}

@Preview(showBackground = true)
@Composable
private fun HabitFormPreview() {
    HabitForm(onValueChange = {}, habitUiState = HabitUiState.Habit())
}