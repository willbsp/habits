package com.willbsp.habits.ui.common.form

import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessAlarm
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.willbsp.habits.R
import com.willbsp.habits.data.model.HabitFrequency
import com.willbsp.habits.domain.model.HabitReminderType
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HabitForm(
    modifier: Modifier = Modifier,
    onValueChange: (HabitFormUiState.Data) -> Unit,
    showTimePicker: (Boolean) -> Unit,
    showDayPicker: (Boolean) -> Unit,
    showNotificationPermissionDialog: (Boolean) -> Unit,
    showAlarmsPermissionDialog: (Boolean) -> Unit,
    habitFormUiState: HabitFormUiState.Data
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        OutlinedTextField(
            value = habitFormUiState.name,
            singleLine = true,
            isError = habitFormUiState.nameIsInvalid,
            onValueChange = { onValueChange(habitFormUiState.copy(name = it)) },
            label = { Text(stringResource(R.string.modify_habit_name)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth()
        )

        HabitFrequencyDropdown(
            modifier = Modifier.fillMaxWidth(),
            uiState = habitFormUiState,
            onValueChange = onValueChange
        )

        HabitReminderDropdown(
            modifier = Modifier.fillMaxWidth(),
            uiState = habitFormUiState,
            onValueChange = onValueChange,
            showTimePicker = showTimePicker,
            showDayPicker = showDayPicker,
            showNotificationPermissionDialog = showNotificationPermissionDialog,
            showAlarmsPermissionDialog = showAlarmsPermissionDialog
        )

    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
private fun HabitReminderDropdown(
    // TODO could make this generic
    modifier: Modifier = Modifier,
    uiState: HabitFormUiState.Data,
    onValueChange: (HabitFormUiState.Data) -> Unit,
    showTimePicker: (Boolean) -> Unit,
    showDayPicker: (Boolean) -> Unit,
    showNotificationPermissionDialog: (Boolean) -> Unit,
    showAlarmsPermissionDialog: (Boolean) -> Unit,
) {

    val reminderOptions = HabitReminderType.values()
    var reminderExpanded by remember { mutableStateOf(false) }
    var reminderSelected by remember { mutableStateOf(uiState.reminderType) }

    val activity = LocalContext.current as Activity
    val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        ExposedDropdownMenuBox(
            modifier = Modifier,
            expanded = reminderExpanded,
            onExpandedChange = { reminderExpanded = !reminderExpanded }
        ) {

            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                value = stringResource(id = reminderSelected.userReadableStringRes),
                onValueChange = {},
                label = { Text(stringResource(R.string.modify_reminder)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = reminderExpanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
            )
            ExposedDropdownMenu(
                expanded = reminderExpanded,
                onDismissRequest = { reminderExpanded = false }
            ) {
                reminderOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(stringResource(selectionOption.userReadableStringRes)) },
                        onClick = {
                            reminderSelected = selectionOption
                            onValueChange(uiState.copy(reminderType = selectionOption))
                            reminderExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms() &&
                uiState.alarmPermissionDialogShown &&
                reminderSelected == HabitReminderType.NONE
            ) {
                onValueChange(uiState.copy(alarmPermissionDialogShown = false))
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationPermission =
                rememberPermissionState(android.Manifest.permission.POST_NOTIFICATIONS)
            if (!notificationPermission.status.isGranted &&
                uiState.notificationPermissionDialogShown &&
                reminderSelected == HabitReminderType.NONE
            ) {
                onValueChange(uiState.copy(notificationPermissionDialogShown = false))
            }
        }

        AnimatedVisibility(visible = (reminderSelected == HabitReminderType.EVERYDAY) || (reminderSelected == HabitReminderType.SPECIFIC)) {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (!alarmManager.canScheduleExactAlarms()) {

                    if (!uiState.alarmPermissionDialogShown) {
                        SideEffect {
                            showAlarmsPermissionDialog(true)
                        }
                    } else {
                        reminderSelected = HabitReminderType.NONE
                        onValueChange(uiState.copy(reminderType = reminderSelected))
                    }

                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                        val notificationPermission =
                            rememberPermissionState(android.Manifest.permission.POST_NOTIFICATIONS)
                        if (!notificationPermission.status.isGranted) {
                            if (!uiState.notificationPermissionDialogShown) {
                                SideEffect {
                                    showNotificationPermissionDialog(true)
                                }
                            } else {
                                reminderSelected = HabitReminderType.NONE
                                onValueChange(uiState.copy(reminderType = reminderSelected))
                            }
                        }

                    }
                }

            }

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HabitReminderTimeField(
                    modifier = Modifier.fillMaxWidth(),
                    showTimePicker = { showTimePicker(true) },
                    time = uiState.reminderTime
                )
                AnimatedVisibility(visible = (reminderSelected == HabitReminderType.SPECIFIC)) {
                    HabitReminderDayField(
                        modifier = Modifier.fillMaxWidth(),
                        showDayPicker = { showDayPicker(true) },
                        isInvalid = uiState.daysIsInvalid,
                        days = uiState.reminderDays
                    )
                }
            }


        }
    }
}

@Composable
private fun HabitReminderTimeField(
    modifier: Modifier = Modifier,
    showTimePicker: () -> Unit,
    time: LocalTime
) {

    val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
    val source = remember { MutableInteractionSource() }

    OutlinedTextField(
        modifier = modifier,
        value = time.format(formatter),
        readOnly = true,
        onValueChange = {},
        label = { Text(stringResource(R.string.modify_reminder_time)) },
        interactionSource = source,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.AccessAlarm,
                contentDescription = null
            )
        }
    )

    if (source.collectIsPressedAsState().value) {
        SideEffect {
            showTimePicker()
        }
    }

}

@Composable
private fun HabitReminderDayField(
    modifier: Modifier = Modifier,
    showDayPicker: () -> Unit,
    isInvalid: Boolean,
    days: Set<DayOfWeek>
) {

    val source = remember { MutableInteractionSource() }

    OutlinedTextField(
        modifier = modifier,
        value = days
            .sorted()
            .map { it.getDisplayName(TextStyle.SHORT, Locale.getDefault()) }
            .toString()
            .drop(1).dropLast(1), // drop [ ]
        readOnly = true,
        isError = isInvalid,
        onValueChange = {},
        label = { Text(stringResource(R.string.modify_reminder_days)) },
        interactionSource = source,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = null
            )
        }
    )

    if (source.collectIsPressedAsState().value) {
        SideEffect {
            showDayPicker()
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HabitFrequencyDropdown(
    modifier: Modifier = Modifier,
    uiState: HabitFormUiState.Data,
    onValueChange: (HabitFormUiState.Data) -> Unit
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
    HabitForm(
        onValueChange = {},
        showTimePicker = {},
        showDayPicker = {},
        showNotificationPermissionDialog = {},
        showAlarmsPermissionDialog = {},
        habitFormUiState = HabitFormUiState.Data()
    )
}