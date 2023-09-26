package com.willbsp.habits.ui.common.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.willbsp.habits.R


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TimePickerDialog(
    modifier: Modifier = Modifier,
    state: TimePickerState,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
) {

    AlertDialog(onDismissRequest = onCancel) {
        Surface(
            modifier = modifier,
            shape = AlertDialogDefaults.shape,
            color = AlertDialogDefaults.containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(
                modifier = modifier.padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                TimePicker(state = state)
                FlowRow(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(
                        modifier = Modifier.testTag("timepicker_dismiss"),
                        onClick = onCancel
                    ) {
                        Text(stringResource(R.string.timepicker_dismiss))
                    }
                    Button(
                        modifier = Modifier.testTag("timepicker_confirm"),
                        onClick = onConfirm
                    ) {
                        Text(stringResource(R.string.timepicker_confirm))
                    }
                }
            }
        }

    }

}
