package com.willbsp.habits.ui.common

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.willbsp.habits.R

// TODO fix requiresapi for lower apis, different way of getting vibrator?
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HabitToggleButton(
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> (Unit),
    checked: Boolean
) {

    /*val v: VibratorManager =
        LocalContext.current.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
    val vibrator: Vibrator = v.defaultVibrator*/

    AnimatedContent(targetState = checked) {
        FilledIconToggleButton(
            modifier = modifier.size(40.dp),
            onCheckedChange = { value ->
                /*vibrator.vibrate(
                    VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK)
                )*/
                onCheckedChange(value)
            },
            checked = it
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