package com.willbsp.habits.ui.common.button

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HabitToggleButton(
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> (Unit),
    checked: Boolean,
    checkedSecondary: Boolean,
    contentDescription: String
) {

    val haptic = LocalHapticFeedback.current
    val icon = if (!checkedSecondary && !checked) Icons.Default.Close
    else Icons.Default.Done

    AnimatedContent(targetState = Pair(checked, icon), label = "HabitToggleButton") {
        FilledIconToggleButton(
            modifier = modifier.size(40.dp),
            onCheckedChange = { value ->
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onCheckedChange(value)
            },
            checked = it.first
        ) {
            Icon(
                imageVector = it.second,
                contentDescription = contentDescription
            )
        }
    }

}

@Preview
@Composable
fun HabitToggleButtonCheckedPreview() {
    HabitToggleButton(
        onCheckedChange = {}, checked = true, checkedSecondary = true, contentDescription = ""
    )
}

@Preview
@Composable
fun HabitToggleButtonUncheckedPreview() {
    HabitToggleButton(
        onCheckedChange = {}, checked = false, checkedSecondary = false, contentDescription = ""
    )
}

@Preview
@Composable
fun HabitToggleButtonCheckedSecondaryPreview() {
    HabitToggleButton(
        onCheckedChange = {}, checked = false, checkedSecondary = true, contentDescription = ""
    )
}
