package com.willbsp.habits.ui.common

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.willbsp.habits.R

@Composable
fun HabitToggleButton(
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> (Unit),
    checked: Boolean
) {
    FilledIconToggleButton(
        modifier = modifier.size(40.dp),
        onCheckedChange = onCheckedChange,
        checked = checked
    ) {
        Icon(
            imageVector = Icons.Default.Done,
            contentDescription = stringResource(id = R.string.home_screen_completed)
        )
    }
}

@Preview
@Composable
fun HabitToggleButtonCheckedPreview() {
    HabitToggleButton(
        onCheckedChange = {}, checked = true
    )
}

@Preview
@Composable
fun HabitToggleButtonUncheckedPreview() {
    HabitToggleButton(
        onCheckedChange = {}, checked = false
    )
}