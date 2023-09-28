package com.willbsp.habits.ui.screens.add

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.willbsp.habits.R
import com.willbsp.habits.ui.common.DefaultHabitsAppTopBar
import com.willbsp.habits.ui.common.button.HabitsFloatingAction
import com.willbsp.habits.ui.common.dialog.AlarmsPermissionDialog
import com.willbsp.habits.ui.common.dialog.DayPickerDialog
import com.willbsp.habits.ui.common.dialog.NotificationPermissionDialog
import com.willbsp.habits.ui.common.dialog.TimePickerDialog
import com.willbsp.habits.ui.common.form.HabitForm
import com.willbsp.habits.ui.common.form.HabitFormUiState
import com.willbsp.habits.ui.theme.HabitsTheme
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
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
        var showNotificationPermissionDialog by remember { mutableStateOf(false) }
        var showAlarmsPermissionDialog by remember { mutableStateOf(false) }

        HabitForm(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = 10.dp)
                .fillMaxSize(),
            onValueChange = onValueChange,
            showTimePicker = {
                showTimePicker = it
            }, // TODO move dialogs into a habit form dialogs composable, then make enum which you pass to say which one to show
            showDayPicker = { showDayPicker = it },
            showNotificationPermissionDialog = {
                showNotificationPermissionDialog = it
            },
            showAlarmsPermissionDialog = {
                showAlarmsPermissionDialog = it
            },
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

        if (showNotificationPermissionDialog && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationPermission =
                rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
            NotificationPermissionDialog(
                onDismiss = {
                    onValueChange(formUiState.copy(notificationPermissionDialogShown = true))
                    showNotificationPermissionDialog = false
                },
                onConfirm = {
                    if (notificationPermission.status.isGranted) {
                        onValueChange(formUiState.copy(notificationPermissionDialogShown = true))
                        showNotificationPermissionDialog = false
                    } else {
                        notificationPermission.launchPermissionRequest()
                    }
                },
                showConfirmButton = !notificationPermission.status.shouldShowRationale || notificationPermission.status.isGranted
            )
        }

        if (showAlarmsPermissionDialog && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val activity = LocalContext.current as Activity
            AlarmsPermissionDialog(
                onDismiss = {
                    onValueChange(formUiState.copy(alarmPermissionDialogShown = true))
                    showAlarmsPermissionDialog = false
                },
                onConfirm = {
                    val alarmManager =
                        activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    if (alarmManager.canScheduleExactAlarms()) {
                        onValueChange(formUiState.copy(alarmPermissionDialogShown = true))
                        showAlarmsPermissionDialog = false
                    } else {
                        val intent = Intent().apply {
                            action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                        }
                        activity.startActivity(intent)
                    }
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
