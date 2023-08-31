package com.willbsp.habits.ui.common.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.willbsp.habits.R
import com.willbsp.habits.ui.theme.Typography
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DayPickerDialog(
    modifier: Modifier = Modifier,
    state: Set<DayOfWeek>,
    onValueChange: (DayOfWeek, Boolean) -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {

    AlertDialog(modifier = modifier, onDismissRequest = onCancel) {
        Surface(
            shape = AlertDialogDefaults.shape,
            color = AlertDialogDefaults.containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(
                modifier = Modifier.padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {

                    for (day in DayOfWeek.values()) {
                        DayPickerCheckboxItem(
                            modifier = Modifier,
                            day = day,
                            checked = state.contains(day),
                            onCheckedChanged = { checked ->
                                onValueChange(day, checked)
                            }
                        )
                    }

                }
                Spacer(Modifier.height(15.dp))
                FlowRow( // TODO could make generic
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(
                        modifier = Modifier.testTag("daypicker_dismiss"),
                        onClick = onCancel
                    ) {
                        Text(stringResource(R.string.timepicker_dismiss))
                    }
                    Button(
                        modifier = Modifier.testTag("daypicker_confirm"),
                        onClick = onConfirm
                    ) {
                        Text(stringResource(R.string.timepicker_confirm))
                    }
                }
            }
        }
    }

}

@Composable
private fun DayPickerCheckboxItem(
    modifier: Modifier = Modifier,
    day: DayOfWeek,
    checked: Boolean,
    onCheckedChanged: (Boolean) -> (Unit)
) {

    val dayName = day.getDisplayName(TextStyle.FULL, Locale.getDefault())

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Checkbox(
            modifier = Modifier.testTag(dayName),
            checked = checked,
            onCheckedChange = onCheckedChanged
        )
        Text(
            text = dayName,
            textAlign = TextAlign.Start,
            style = Typography.labelLarge
        )
    }

}

@Preview
@Composable
private fun DayPickerDialogPreview() {
    DayPickerDialog(
        modifier = Modifier.width(300.dp),
        state = mutableSetOf(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.SUNDAY),
        onCancel = { },
        onValueChange = { a, b -> },
        onConfirm = {}
    )
}