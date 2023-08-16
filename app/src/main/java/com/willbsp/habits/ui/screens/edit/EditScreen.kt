package com.willbsp.habits.ui.screens.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun EditScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onValueChange: (HabitFormUiState) -> Unit,
    formUiState: HabitFormUiState
) {

    Scaffold(
        topBar = {
            DefaultHabitsAppTopBar(
                title = stringResource(R.string.edit_habit_title),
                canNavigateBack = true,
                navigateUp = navigateUp
            )
        },
        floatingActionButton = {
            HabitsFloatingAction(
                onClick = onSaveClick,
                icon = Icons.Default.Done,
                contentDescription = stringResource(R.string.edit_habit_update_habit)
            )
        }
    ) { innerPadding ->

        when (formUiState) {

            is HabitFormUiState.Data -> {

                val timePickerState = rememberTimePickerState(
                    initialHour = formUiState.reminderTime.hour,
                    initialMinute = formUiState.reminderTime.minute
                )
                var dayPickerState by remember { mutableStateOf(formUiState.reminderDays) }
                var showTimePicker by remember { mutableStateOf(false) }
                var showDayPicker by remember { mutableStateOf(false) }
                var deleteDialogOpen by remember { mutableStateOf(false) }

                Column(
                    modifier = modifier
                        .padding(innerPadding)
                        .padding(horizontal = 10.dp)
                        .fillMaxSize()
                ) {

                    HabitForm(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onValueChange = onValueChange,
                        showTimePicker = { showTimePicker = it },
                        showDayPicker = { showDayPicker = it },
                        habitFormUiState = formUiState,
                        showNotificationPermissionDialog = { _ -> },
                        showAlarmsPermissionDialog = { _ -> }
                    )

                    Spacer(Modifier.height(10.dp))

                    FilledTonalButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { deleteDialogOpen = true }
                    ) {
                        Text(stringResource(R.string.edit_habit_delete))
                    }

                }

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

                if (deleteDialogOpen) {
                    EditDeleteDialog(
                        onDismiss = { deleteDialogOpen = false },
                        onConfirm = {
                            deleteDialogOpen = false
                            onDeleteClick()
                        }
                    )
                }

            }

            else -> {}

        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EditScreenPreview() {
    HabitsTheme {
        EditScreen(
            navigateUp = {},
            onSaveClick = {},
            onDeleteClick = {},
            onValueChange = {},
            formUiState = HabitFormUiState.Loading
        )
    }
}
