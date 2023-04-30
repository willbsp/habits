package com.willbsp.habits.ui.common

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.willbsp.habits.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HabitToggleButton(
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> (Unit),
    checked: Boolean,
    checkedSecondary: Boolean
) {

    val haptic = LocalHapticFeedback.current

    val colors = if (checkedSecondary) IconButtonDefaults.filledIconToggleButtonColors(
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        containerColor = MaterialTheme.colorScheme.tertiaryContainer
    ) else IconButtonDefaults.filledIconToggleButtonColors()

    AnimatedContent(targetState = checked) {
        FilledIconToggleButton(
            modifier = modifier.size(40.dp),
            onCheckedChange = { value ->
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onCheckedChange(value)
            },
            checked = it,
            colors = colors
        ) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = stringResource(id = R.string.home_completed)
            )
        }
    }

}

@Preview
@Composable
fun HabitToggleButtonCheckedPreview() {
    HabitToggleButton(
        onCheckedChange = {}, checked = true, checkedSecondary = true
    )
}

@Preview
@Composable
fun HabitToggleButtonUncheckedPreview() {
    HabitToggleButton(
        onCheckedChange = {}, checked = false, checkedSecondary = false
    )
}

@Preview
@Composable
fun HabitToggleButtonCheckedSecondaryPreview() {
    HabitToggleButton(
        onCheckedChange = {}, checked = false, checkedSecondary = true
    )
}
