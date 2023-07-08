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
import com.willbsp.habits.data.model.HabitFrequency
import com.willbsp.habits.ui.theme.Typography
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

    val (dailyHabitsList, weeklyHabitsList) = remember(homeUiState.habits) {
        homeUiState.habits.partition { it.type == HabitFrequency.DAILY }
    }
    val dailyCompleted = remember(dailyHabitsList) {
        dailyHabitsList.map { it.hasBeenCompleted(homeUiState.todaysDate) }.all { it }
    }
    val weeklyCompleted = remember(weeklyHabitsList) {
        weeklyHabitsList.map { it.hasBeenCompleted(homeUiState.todaysDate) }.all { it }
    }

    LazyColumn(modifier = modifier) {
        this.stickyHeader {
            HabitListStickyHeader(
                visible = (!dailyCompleted || showCompleted) && dailyHabitsList.isNotEmpty(),
                text = stringResource(R.string.home_today)
            )
        }
        items(
            items = dailyHabitsList,
            key = { it.id }
        ) { habit ->
            HabitListCard(
                modifier = Modifier
                    .animateItemPlacement(tween())
                    .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                visible = !habit.hasBeenCompleted(homeUiState.todaysDate) || showCompleted,
                habit = habit,
                todaysDate = homeUiState.todaysDate,
                completedOnClick = completedOnClick,
                navigateToDetail = navigateToDetail,
                showStatistic = homeUiState.showStreaks,
                showScore = homeUiState.showScore
            )
        }
        this.stickyHeader {
            HabitListStickyHeader(
                visible = (!weeklyCompleted || showCompleted) && weeklyHabitsList.isNotEmpty(),
                text = stringResource(R.string.home_this_week)
            )
        }
        items(
            items = weeklyHabitsList,
            key = { it.id }
        ) { habit ->
            HabitListCard(
                modifier = Modifier
                    .animateItemPlacement(tween())
                    .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                visible = !habit.hasBeenCompleted(homeUiState.todaysDate) || showCompleted,
                habit = habit,
                todaysDate = homeUiState.todaysDate,
                completedOnClick = completedOnClick,
                navigateToDetail = navigateToDetail,
                showStatistic = homeUiState.showStreaks,
                showScore = homeUiState.showScore
            )
        }
        item {
            val completedCount = remember(homeUiState.habits) {
                homeUiState.habits.count { it.hasBeenCompleted(homeUiState.todaysDate) }
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
            // Spacer at the bottom ensures that FAB does not obscure habits at the bottom of the list
            Spacer(modifier.height(100.dp))
        }
    }

}

@Composable
private fun HabitListCard(
    modifier: Modifier = Modifier,
    visible: Boolean,
    todaysDate: LocalDate,
    habit: HomeUiState.Habit,
    completedOnClick: (Int, LocalDate) -> Unit,
    navigateToDetail: (Int) -> Unit,
    showStatistic: Boolean,
    showScore: Boolean
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
            showStatistic = showStatistic,
            showScore = showScore,
            todaysDate = todaysDate
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
