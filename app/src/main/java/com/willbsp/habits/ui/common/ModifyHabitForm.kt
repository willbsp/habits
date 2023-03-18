package com.willbsp.habits.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.willbsp.habits.R
import com.willbsp.habits.data.model.HabitFrequency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyHabitForm(
    modifier: Modifier = Modifier,
    onValueChange: (ModifyHabitUiState) -> Unit,
    habitUiState: ModifyHabitUiState
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

        TextFieldDropdown(
            onValueChange = onValueChange,
            habitUiState = habitUiState
        )

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextFieldDropdown( // TODO better animated dropdown
    modifier: Modifier = Modifier,
    onValueChange: (ModifyHabitUiState) -> Unit,
    habitUiState: ModifyHabitUiState
) {

    var expanded by remember { mutableStateOf(false) }
    var stringResource by remember { mutableStateOf(habitUiState.frequency.userReadableStringRes) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            readOnly = true,
            value = stringResource(stringResource),
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text(stringResource(R.string.modify_habit_frequency)) },
            trailingIcon = {
                Icon(icon, stringResource(R.string.modify_habit_frequency),
                    Modifier.clickable {
                        expanded = !expanded
                    }
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
        ) {
            HabitFrequency.values().forEach { frequency ->
                DropdownMenuItem(
                    text = { Text(stringResource(frequency.userReadableStringRes)) },
                    onClick = {
                        onValueChange(habitUiState.copy(frequency = frequency))
                        stringResource = frequency.userReadableStringRes
                        expanded = false
                    }
                )
            }
        }

    }
}