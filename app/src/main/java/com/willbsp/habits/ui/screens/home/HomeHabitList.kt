package com.willbsp.habits.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.willbsp.habits.R
import com.willbsp.habits.common.rangeTo
import com.willbsp.habits.data.model.HabitFrequency
import com.willbsp.habits.ui.theme.Typography
import java.time.DayOfWeek
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeHabitList(
    homeUiState: HomeUiState.Habits,
    completedOnClick: (Int, LocalDate) -> Unit,
    navigateToDetail: (Int) -> Unit,
    showCompleted: Boolean,
    modifier: Modifier = Modifier
) {

    val dailyHabitsList = remember(homeUiState.habits) {
        homeUiState.habits.filter { it.type == HabitFrequency.DAILY }
    }
    val weeklyHabitsList = remember(homeUiState.habits) {
        homeUiState.habits.filter { it.type == HabitFrequency.WEEKLY }
    }
    val weekDates = remember {
        (LocalDate.now().with(DayOfWeek.MONDAY)..LocalDate.now()).toList()
    }

    val dailyCompleted = remember(dailyHabitsList) {
        dailyHabitsList.map { habit ->
            habit.completed.contains(LocalDate.now())
        }.all { it }
    }
    val weeklyCompleted = remember(weeklyHabitsList) {
        weeklyHabitsList.map { habit ->
            (habit.completed + habit.completedByWeek).containsAll(weekDates)
        }.all { it }
    }

    LazyColumn(modifier = modifier) {
        this.stickyHeader {
            HabitListStickyHeader(
                visible = !dailyCompleted || showCompleted,
                text = stringResource(R.string.home_today)
            )
        }
        items(
            items = dailyHabitsList,
            key = { it.id }
        ) { habit ->
            val habitCompleted = remember(habit.completed) {
                habit.completed.any { it == LocalDate.now() }
            }
            HabitListCard(
                modifier = Modifier
                    .animateItemPlacement(tween())
                    .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                visible = !habitCompleted || showCompleted,
                habit = habit,
                completedOnClick = completedOnClick,
                navigateToDetail = navigateToDetail,
                showStreaks = homeUiState.showStreaks
            )
        }
        item {
            val completedCount = remember(dailyHabitsList) {
                dailyHabitsList.count { habit ->
                    habit.completed.any { it == LocalDate.now() }
                }
            }
            if (homeUiState.showSubtitle) {
                HabitListSubtitle(
                    modifier = Modifier.fillMaxWidth(),
                    visible = completedCount > 0 && !showCompleted,
                    text = pluralStringResource(
                        id = R.plurals.home_habit_list_subtitle,
                        count = completedCount,
                        completedCount
                    )
                )
            }
            Spacer(Modifier.height(20.dp))
        }
        this.stickyHeader {
            HabitListStickyHeader(
                visible = !weeklyCompleted || showCompleted,
                text = stringResource(R.string.home_this_week)
            )
        }
        items(
            items = weeklyHabitsList,
            key = { it.id }
        ) { habit ->
            val weekCompleted = remember(habit) {
                (habit.completed + habit.completedByWeek).containsAll(weekDates)
            }
            HabitListCard(
                modifier = Modifier
                    .animateItemPlacement(tween())
                    .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                visible = !weekCompleted || showCompleted,
                habit = habit,
                completedOnClick = completedOnClick,
                navigateToDetail = navigateToDetail,
                showStreaks = homeUiState.showStreaks
            )
        }
        item {
            val weekCompletedCount = remember(weeklyHabitsList) {
                weeklyHabitsList.count { habit ->
                    (habit.completed + habit.completedByWeek).containsAll(weekDates)
                }
            }
            if (homeUiState.showSubtitle) {
                HabitListSubtitle(
                    modifier = Modifier.fillMaxWidth(),
                    visible = weekCompletedCount > 0 && !showCompleted,
                    text = pluralStringResource(
                        id = R.plurals.home_habit_list_weekly_subtitle,
                        count = weekCompletedCount,
                        weekCompletedCount
                    )
                )
            }
            // Spacer at the bottom ensures that FAB does not obscure habits at the bottom of the list
            Spacer(modifier.height(100.dp))
        }
    }

}

@Composable
private fun HabitListCard(
    modifier: Modifier = Modifier,
    visible: Boolean,
    habit: HomeUiState.Habit,
    completedOnClick: (Int, LocalDate) -> Unit,
    navigateToDetail: (Int) -> Unit,
    showStreaks: Boolean
) {
    AnimatedVisibility(
        visible = visible,
        exit = shrinkVertically(animationSpec = TweenSpec(delay = 200)),
        enter = expandVertically()
    ) {

        HomeHabitCard(
            modifier = modifier,
            habit = habit,
            completedOnClick = completedOnClick,
            navigateToDetail = navigateToDetail,
            showStreaks = showStreaks
        )

    }
}

@Composable
private fun HabitListStickyHeader(
    visible: Boolean,
    text: String
) {
    AnimatedVisibility(
        visible = visible,
        exit = shrinkVertically(animationSpec = TweenSpec(delay = 200)),
        enter = expandVertically()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                text = text,
                style = Typography.titleLarge,
                modifier = Modifier
                    .padding(start = 30.dp, bottom = 10.dp) // keep inline with habit titles
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun HabitListSubtitle(
    modifier: Modifier = Modifier,
    visible: Boolean,
    text: String
) {
    AnimatedVisibility(
        visible = visible,
        exit = shrinkVertically(animationSpec = TweenSpec(delay = 200)),
        enter = expandVertically()
    ) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = Typography.labelLarge
            )
        }
    }
}