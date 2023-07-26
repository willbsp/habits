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
import com.willbsp.habits.ui.common.HabitForm
import com.willbsp.habits.ui.common.HabitUiState
import com.willbsp.habits.ui.common.HabitsFloatingAction
import com.willbsp.habits.ui.common.TimePickerDialog
import com.willbsp.habits.ui.theme.HabitsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    onSaveClick: () -> Unit,
    onValueChange: (HabitUiState.Habit) -> Unit,
    habitUiState: HabitUiState.Habit
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

        // TODO for 24 hour find users preference
        val timePickerState = rememberTimePickerState(is24Hour = true)
        var showTimePicker by remember { mutableStateOf(false) }

        HabitForm(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = 10.dp)
                .fillMaxSize(),
            onValueChange = onValueChange,
            showTimePicker = { showTimePicker = it },
            habitUiState = habitUiState
        )

        if (showTimePicker) {
            TimePickerDialog(
                state = timePickerState,
                onCancel = { showTimePicker = false },
                onConfirm = { showTimePicker = false }
            )
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddHabitScreenPreview() {
    HabitsTheme {
        AddHabitScreen(
            navigateUp = {},
            onSaveClick = {},
            onValueChange = {},
            habitUiState = HabitUiState.Habit()
        )
    }
}
