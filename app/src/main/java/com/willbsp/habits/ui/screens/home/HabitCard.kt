package com.willbsp.habits.ui.screens.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.willbsp.habits.R
import com.willbsp.habits.ui.theme.Typography

@Composable
fun HabitCard(
    habitUiState: HomeHabitUiState,
    completedOnClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    var expanded by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = modifier.wrapContentHeight(),
        colors = CardDefaults.cardColors()
    ) {

        Box(
            modifier = Modifier.clickable {
                expanded = !expanded
            },
        ) {

            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .animateContentSize()
            ) {


                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = habitUiState.name,
                        style = Typography.titleLarge,
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    HabitToggleButton(
                        onCheckedChange = {
                            completedOnClick(habitUiState.id)
                        },
                        checked = habitUiState.completed
                    )

                }

                if (expanded) {

                    Row(
                        modifier = Modifier
                            .height(80.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        HabitCardDaysRow()

                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(
                            modifier = Modifier,
                            onClick = {}
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = stringResource(R.string.home_screen_completed)
                            )
                        }

                    }
                }

            }
        }

    }
}

@Composable
fun HabitCardDaysRow(
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        HabitCardDay(day = "M")
        HabitCardDay(day = "S")
        HabitCardDay(day = "S")
        HabitCardDay(day = "F")
        HabitCardDay(day = "T")

    }

}

@Composable
fun HabitCardDay(
    modifier: Modifier = Modifier,
    day: String
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = day,
            style = Typography.bodyMedium
        )

        HabitToggleButton(
            onCheckedChange = {},
            checked = true
        )

    }

}

@Composable
fun HabitToggleButton(
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> (Unit),
    checked: Boolean
) {
    IconToggleButton(
        modifier = modifier.size(40.dp),
        onCheckedChange = onCheckedChange,
        checked = checked,
        colors = IconButtonDefaults.filledIconToggleButtonColors()
    ) {
        Icon(
            imageVector = Icons.Default.Done,
            contentDescription = stringResource(id = R.string.home_screen_completed)
        )
    }
}

@Preview
@Composable
fun HabitCardPreview() {
    HabitCard(
        habitUiState = HomeHabitUiState(
            id = 1,
            name = "Reading",
            completed = true
        ),
        completedOnClick = {}
    )
}