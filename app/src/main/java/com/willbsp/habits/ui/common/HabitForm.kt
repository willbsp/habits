package com.willbsp.habits.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.willbsp.habits.R
import com.willbsp.habits.data.model.HabitFrequency

@OptIn(ExperimentalMaterial3Api::class)
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

    val options = HabitFrequency.values()
    var expanded by remember { mutableStateOf(false) }
    var selectedFrequency by remember { mutableStateOf(uiState.frequency) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            value = stringResource(id = selectedFrequency.userReadableStringRes),
            onValueChange = {},
            label = { Text("Frequency") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(stringResource(id = selectionOption.userReadableStringRes)) },
                    onClick = {
                        selectedFrequency = selectionOption
                        onValueChange(uiState.copy(frequency = selectionOption))
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }

}