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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.willbsp.habits.R
import com.willbsp.habits.ui.theme.Typography

@Composable
fun HabitCard(
    habitUiState: HomeHabitUiState,
    completedOnClick: (Int) -> Unit,
    navigateToEditHabit: (Int) -> Unit,
    modifier: Modifier = Modifier,
    expandedInitialValue: Boolean = false
) {

    var expanded by remember { mutableStateOf(expandedInitialValue) }

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
                        .height(50.dp)
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

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        HabitCardDaysRow()

                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(
                            modifier = Modifier,
                            onClick = { navigateToEditHabit(habitUiState.id) }
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
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {

        HabitCardDay(weekday = "Mon\n21")
        HabitCardDay(weekday = "Sun\n20")
        HabitCardDay(weekday = "Sat\n19")
        HabitCardDay(weekday = "Fri\n18")
        HabitCardDay(weekday = "Thu\n17")

    }

}

@Composable
fun HabitCardDay(
    modifier: Modifier = Modifier,
    weekday: String
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = weekday,
            style = Typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

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
        completedOnClick = {},
        navigateToEditHabit = {},
        expandedInitialValue = false
    )
}

@Preview
@Composable
fun HabitCardExpandedPreview() {
    HabitCard(
        habitUiState = HomeHabitUiState(
            id = 1,
            name = "Walking",
            completed = true
        ),
        completedOnClick = {},
        navigateToEditHabit = {},
        expandedInitialValue = true
    )
}