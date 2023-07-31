package com.willbsp.habits.ui.screens.add

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.willbsp.habits.R
import com.willbsp.habits.ui.common.DefaultHabitsAppTopBar
import com.willbsp.habits.ui.common.button.HabitsFloatingAction
import com.willbsp.habits.ui.common.dialog.DayPickerDialog
import com.willbsp.habits.ui.common.dialog.TimePickerDialog
import com.willbsp.habits.ui.common.form.HabitForm
import com.willbsp.habits.ui.common.form.HabitFormUiState
import com.willbsp.habits.ui.theme.HabitsTheme
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    onSaveClick: () -> Unit,
    onValueChange: (HabitFormUiState.Data) -> Unit,
    formUiState: HabitFormUiState.Data
) {

    Scaffold(
        topBar = {
            DefaultHabitsAppTopBar(
                title = stringResource(R.string.add_habit_title),
                canNavigateBack = true,
                navigateUp = navigateUp
            )
        },
        floatingActionButton = {
            HabitsFloatingAction(
                onClick = onSaveClick,
                icon = Icons.Default.Done,
                contentDescription = stringResource(R.string.add_habit_add_habit)
            )
        }
    ) { innerPadding ->

        val timePickerState = rememberTimePickerState(
            initialHour = formUiState.reminderTime.hour,
            initialMinute = formUiState.reminderTime.minute
        )
        var dayPickerState by remember { mutableStateOf(formUiState.reminderDays) }
        var showTimePicker by remember { mutableStateOf(false) }
        var showDayPicker by remember { mutableStateOf(false) }

        HabitForm(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = 10.dp)
                .fillMaxSize(),
            onValueChange = onValueChange,
            showTimePicker = { showTimePicker = it },
            showDayPicker = { showDayPicker = it },
            habitFormUiState = formUiState
        )

        if (showTimePicker) {
            TimePickerDialog(
                state = timePickerState,
                onCancel = { showTimePicker = false },
                onConfirm = {
                    onValueChange(
                        formUiState.copy(
                            reminderTime = LocalTime.of(
                                timePickerState.hour,
                                timePickerState.minute
                            )
                        )
                    )
                    showTimePicker = false
                }
            )
        }

        if (showDayPicker) {
            DayPickerDialog(
                state = dayPickerState,
                onCancel = { showDayPicker = false },
                onValueChange = { day, checked ->
                    val newState = dayPickerState.toMutableSet()
                    if (checked) {
                        newState.add(day)
                    } else {
                        newState.remove(day)
                    }
                    dayPickerState = newState.toSet()
                },
                onConfirm = {
                    onValueChange(formUiState.copy(reminderDays = dayPickerState))
                    showDayPicker = false
                }
            )
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddHabitScreenPreview() {
    HabitsTheme {
        AddScreen(
            navigateUp = {},
            onSaveClick = {},
            onValueChange = {},
            formUiState = HabitFormUiState.Data()
        )
    }
}
