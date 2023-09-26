package com.willbsp.habits.ui.screens.home

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.willbsp.habits.R
import com.willbsp.habits.data.model.HabitFrequency
import com.willbsp.habits.ui.common.button.HabitToggleButton
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
    showStatistic: Boolean,
    showScore: Boolean,
    todaysDate: LocalDate,
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
                    AnimatedContent(
                        targetState = Pair(habit.score, habit.streak),
                        label = "Statistic"
                    ) {
                        if (showStatistic) {
                            if (showScore) {
                                Text(
                                    text = if (habit.score != null) "${it.first}%" else " ",
                                    style = Typography.titleLarge
                                )
                            } else {
                                Text(
                                    text = (it.second ?: " ").toString(),
                                    style = Typography.titleLarge
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    HabitToggleButton(
                        onCheckedChange = { completedOnClick(habit.id, todaysDate) },
                        checked = habit.completed.any { it == todaysDate },
                        checkedSecondary = habit.completedByWeek.any { it == todaysDate },
                        contentDescription = "${todaysDate.dayOfWeek} ${todaysDate.dayOfMonth}"
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
                            todaysDate = todaysDate,
                            completedOnClick = completedOnClick
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            modifier = Modifier,
                            onClick = { navigateToDetail(habit.id) }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.MoreHoriz,
                                contentDescription = stringResource(R.string.home_detail)
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
    todaysDate: LocalDate,
    completedOnClick: (Int, LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {

    BoxWithConstraints {

        val days: Int = (maxWidth.value / 60).toInt()

        Row(
            modifier = modifier.width(maxWidth.value.dp - 45.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            (1..days).forEach { i ->
                val date = todaysDate.minusDays(i.toLong())
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
            modifier = Modifier.height(50.dp),
            text = (weekday + "\n" + dayOfMonth),
            style = Typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        HabitToggleButton(
            onCheckedChange = onCheckedChange,
            checked = completed,
            checkedSecondary = completedByWeek,
            contentDescription = "${date.dayOfWeek} ${date.dayOfMonth}"
        )

    }

}


@Preview
@Composable
private fun HomeHabitCardPreview() {
    HomeHabitCard(
        habit = HomeUiState.Habit(
            id = 1,
            name = "Reading",
            type = HabitFrequency.DAILY,
            streak = null,
            score = 43,
            completed = listOf(),
            completedByWeek = listOf()
        ),
        completedOnClick = { _, _ -> },
        navigateToDetail = {},
        expandedInitialValue = false,
        showStatistic = true,
        showScore = true,
        todaysDate = LocalDate.parse("2023-07-09")
    )
}

@Preview
@Composable
private fun HomeHabitCardExpandedPreview() {
    HomeHabitCard(
        habit = HomeUiState.Habit(
            id = 1,
            name = "Walking",
            type = HabitFrequency.DAILY,
            streak = 2,
            score = null,
            completed = listOf(
                LocalDate.parse("2023-07-07"),
                LocalDate.parse("2023-07-05"),
                LocalDate.parse("2023-07-08")
            ),
            completedByWeek = listOf()
        ),
        completedOnClick = { _, _ -> },
        navigateToDetail = {},
        expandedInitialValue = true,
        showStatistic = true,
        showScore = false,
        todaysDate = LocalDate.parse("2023-07-09")
    )
}

@Preview(widthDp = 300)
@Composable
private fun HomeHabitCardExpandedPreview2() {
    HomeHabitCard(
        habit = HomeUiState.Habit(
            id = 1,
            name = "10 pages",
            type = HabitFrequency.DAILY,
            streak = 2,
            score = null,
            completed = listOf(
                LocalDate.parse("2023-07-07"),
                LocalDate.parse("2023-07-05"),
                LocalDate.parse("2023-07-08")
            ),
            completedByWeek = listOf()
        ),
        completedOnClick = { _, _ -> },
        navigateToDetail = {},
        expandedInitialValue = true,
        showStatistic = true,
        showScore = false,
        todaysDate = LocalDate.parse("2023-07-09")
    )
}
