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
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@Composable
fun HabitCard(
    habitUiState: HomeHabitUiState,
    completedOnClick: (Int, String) -> Unit,
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
                            completedOnClick(
                                habitUiState.id,
                                habitUiState.completedDates.first().date // TODO more robust way?
                            )
                        },
                        checked = habitUiState.completedDates.first().completed
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

                        HabitCardDaysRow(
                            habitUiState = habitUiState,
                            completedOnClick = completedOnClick
                        )

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
    habitUiState: HomeHabitUiState,
    completedOnClick: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {

        habitUiState.completedDates.drop(1).forEach { completedState ->
            HabitCardDay(
                date = completedState.date,
                checked = completedState.completed,
                onCheckedChange = {
                    completedOnClick(habitUiState.id, completedState.date)
                }
            ) // curr date -1
        }

    }

}

@Composable
fun HabitCardDay(
    modifier: Modifier = Modifier,
    date: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {

    val weekday = LocalDate.parse(date).dayOfWeek.getDisplayName(
        TextStyle.SHORT,
        Locale.ENGLISH
    ) // TODO get other locales
    val dayOfMonth = LocalDate.parse(date).dayOfMonth.toString()

    Column(
        modifier = modifier.width(45.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier.height(45.dp),
            text = (weekday + "\n" + dayOfMonth),
            style = Typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        HabitToggleButton(
            onCheckedChange = onCheckedChange,
            checked = checked
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
            completedDates = listOf()
        ),
        completedOnClick = { _, _ -> },
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
            completedDates = listOf()
        ),
        completedOnClick = { _, _ -> },
        navigateToEditHabit = {},
        expandedInitialValue = true
    )
}