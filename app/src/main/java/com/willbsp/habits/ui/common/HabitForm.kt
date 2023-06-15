package com.willbsp.habits.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.willbsp.habits.R
import com.willbsp.habits.data.model.HabitFrequency

@Composable
fun HabitForm(
    modifier: Modifier = Modifier,
    onValueChange: (HabitUiState.Habit) -> Unit,
    habitUiState: HabitUiState.Habit
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = habitUiState.name,
            singleLine = true,
            isError = habitUiState.nameIsInvalid,
            onValueChange = { onValueChange(habitUiState.copy(name = it)) },
            label = { Text(stringResource(R.string.modify_habit_name)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        HabitFrequencyDropdown(
            modifier = Modifier.fillMaxWidth(),
            uiState = habitUiState,
            onValueChange = onValueChange
        )

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
        modifier = modifier
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

        Spacer(modifier = Modifier.height(10.dp))

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