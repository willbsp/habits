package com.willbsp.habits.ui.screens.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.willbsp.habits.R
import com.willbsp.habits.ui.common.HabitToggleButton
import com.willbsp.habits.ui.theme.Typography
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@Composable
fun HomeHabitCard(
    habitUiState: HomeHabitUiState,
    completedOnClick: (Int, LocalDate) -> Unit,
    navigateToEditHabit: (Int) -> Unit,
    modifier: Modifier = Modifier,
    showStreaks: Boolean,
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

                    if (showStreaks) {
                        Text(
                            text = (habitUiState.streak ?: " ").toString(),
                            style = Typography.titleLarge
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

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

                        HomeHabitCardDayRow(
                            habitUiState = habitUiState,
                            completedOnClick = completedOnClick
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(
                            modifier = Modifier,
                            onClick = { navigateToEditHabit(habitUiState.id) }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.MoreHoriz,
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
private fun HomeHabitCardDayRow(
    habitUiState: HomeHabitUiState,
    completedOnClick: (Int, LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {

        habitUiState.completedDates.drop(1).forEach { completedState ->
            HomeHabitCardDay(
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
private fun HomeHabitCardDay(
    modifier: Modifier = Modifier,
    date: LocalDate,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {

    val weekday = date.dayOfWeek.getDisplayName(
        TextStyle.SHORT,
        Locale.ENGLISH
    ) // TODO get other locales
    val dayOfMonth = date.dayOfMonth.toString()

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


@Preview
@Composable
private fun HomeHabitCardPreview() {
    HomeHabitCard(
        habitUiState = HomeHabitUiState(
            id = 1,
            name = "Reading",
            streak = 4,
            completedDates = listOf(
                HomeCompletedUiState(LocalDate.parse("2023-04-12"), false),
                HomeCompletedUiState(LocalDate.parse("2023-04-11"), true),
                HomeCompletedUiState(LocalDate.parse("2023-04-10"), false),
                HomeCompletedUiState(LocalDate.parse("2023-04-09"), true),
                HomeCompletedUiState(LocalDate.parse("2023-04-08"), false),
                HomeCompletedUiState(LocalDate.parse("2023-04-07"), true),
            )
        ),
        completedOnClick = { _, _ -> },
        navigateToEditHabit = {},
        expandedInitialValue = false,
        showStreaks = true
    )
}

@Preview
@Composable
private fun HomeHabitCardExpandedPreview() {
    HomeHabitCard(
        habitUiState = HomeHabitUiState(
            id = 1,
            name = "Walking",
            streak = 2,
            completedDates = listOf(
                HomeCompletedUiState(LocalDate.parse("2023-04-12"), true),
                HomeCompletedUiState(LocalDate.parse("2023-04-11"), true),
                HomeCompletedUiState(LocalDate.parse("2023-04-10"), false),
                HomeCompletedUiState(LocalDate.parse("2023-04-09"), true),
                HomeCompletedUiState(LocalDate.parse("2023-04-08"), false),
                HomeCompletedUiState(LocalDate.parse("2023-04-07"), false),
            )
        ),
        completedOnClick = { _, _ -> },
        navigateToEditHabit = {},
        expandedInitialValue = true,
        showStreaks = true
    )
}