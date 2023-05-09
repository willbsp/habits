package com.willbsp.habits.ui.screens.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.willbsp.habits.R
import com.willbsp.habits.ui.common.HabitToggleButton
import com.willbsp.habits.ui.theme.Typography
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HomeHabitCard(
    habit: HomeUiState.Habit,
    completedOnClick: (Int, LocalDate) -> Unit,
    navigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    showStreaks: Boolean,
    expandedInitialValue: Boolean = false
) {

    var expanded by rememberSaveable { mutableStateOf(expandedInitialValue) }

    ElevatedCard(
        modifier = modifier.wrapContentHeight(),
        colors = CardDefaults.cardColors()
    ) {

        Box(
            modifier = Modifier.clickable { expanded = !expanded },
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

                    Text(text = habit.name, style = Typography.titleLarge)
                    Spacer(modifier = Modifier.weight(1f))
                    if (showStreaks) {
                        Text(
                            text = (habit.streak ?: " ").toString(),
                            style = Typography.titleLarge
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    HabitToggleButton(
                        onCheckedChange = { completedOnClick(habit.id, LocalDate.now()) },
                        checked = habit.completed.firstOrNull() == LocalDate.now(),
                        checkedSecondary = habit.completedByWeek.firstOrNull() == LocalDate.now()
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
                            habit = habit,
                            completedOnClick = completedOnClick
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            modifier = Modifier,
                            onClick = { navigateToDetail(habit.id) }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.MoreHoriz,
                                contentDescription = stringResource(R.string.home_completed)
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
    habit: HomeUiState.Habit,
    completedOnClick: (Int, LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {

    BoxWithConstraints {

        val days : Int = (maxWidth.value / 60).toInt()

        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {

            (1..days).forEach { i ->
                val date = LocalDate.now().minusDays(i.toLong())
                HomeHabitCardDay(
                    onCheckedChange = { completedOnClick(habit.id, date) },
                    completed = habit.completed.any { it == date },
                    completedByWeek = habit.completedByWeek.any { it == date },
                    date = date
                )
            }

        }

    }

}

@Composable
private fun HomeHabitCardDay(
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit,
    completed: Boolean,
    completedByWeek: Boolean,
    date: LocalDate
) {

    val weekday = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
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
            checked = completed,
            checkedSecondary = completedByWeek
        )

    }

}


/*@Preview
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
        navigateToDetail = {},
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
        navigateToDetail = {},
        expandedInitialValue = true,
        showStreaks = true
    )
}*/