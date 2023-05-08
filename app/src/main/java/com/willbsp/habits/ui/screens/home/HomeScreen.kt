package com.willbsp.habits.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.willbsp.habits.R
import com.willbsp.habits.common.rangeTo
import com.willbsp.habits.data.model.HabitFrequency
import com.willbsp.habits.ui.common.FullscreenHint
import com.willbsp.habits.ui.common.HabitsFloatingAction
import com.willbsp.habits.ui.theme.HabitsTheme
import com.willbsp.habits.ui.theme.Typography
import java.time.DayOfWeek
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    completedOnClick: (Int, LocalDate) -> Unit,
    navigateToLogbook: () -> Unit,
    navigateToAddHabit: () -> Unit,
    navigateToDetail: (Int) -> Unit,
    navigateToSettings: () -> Unit,
    homeUiState: HomeUiState
) {

    var showCompleted by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateToLogbook) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = ""
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        showCompleted = !showCompleted
                    }) {
                        Icon(
                            imageVector = if (showCompleted) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = stringResource(R.string.home_show_completed)
                        )
                    }
                    IconButton(onClick = navigateToSettings) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = stringResource(R.string.settings)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            HabitsFloatingAction(
                onClick = navigateToAddHabit,
                icon = Icons.Default.Add,
                contentDescription = stringResource(R.string.home_add_habit)
            )
        }
    ) { innerPadding ->

        when (homeUiState) {
            is HomeUiState.Empty -> {
                FullscreenHint(
                    modifier = Modifier.fillMaxSize(),
                    icon = Icons.Default.Add,
                    iconContentDescription = R.string.home_all_completed_tick,
                    text = R.string.home_no_habits
                )
            }

            is HomeUiState.Habits -> {

                val allCompleted = remember(homeUiState) {
                    homeUiState.habits.all { it.completed.firstOrNull() == LocalDate.now() }
                }
                val showHabits = !showCompleted && allCompleted

                AnimatedVisibility(
                    visible = showHabits,
                    enter = scaleIn(TweenSpec(delay = 400)),
                    exit = scaleOut()
                ) {
                    FullscreenHint(
                        modifier = Modifier.fillMaxSize(),
                        icon = Icons.Default.Done,
                        iconContentDescription = R.string.home_all_completed_tick,
                        text = R.string.home_all_completed
                    )
                }

                AnimatedVisibility(
                    visible = !showHabits,
                    enter = fadeIn(),
                    exit = fadeOut(TweenSpec(delay = 200))
                ) {
                    Column(
                        modifier = modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        //Spacer(modifier = Modifier.height(10.dp))
                        HabitsList(
                            homeUiState = homeUiState,
                            completedOnClick = completedOnClick,
                            navigateToDetail = navigateToDetail,
                            showCompleted = showCompleted
                        )
                    }
                }

            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HabitsList(
    homeUiState: HomeUiState.Habits,
    completedOnClick: (Int, LocalDate) -> Unit,
    navigateToDetail: (Int) -> Unit,
    showCompleted: Boolean,
    modifier: Modifier = Modifier
) {

    //val habitsList = homeUiState.habits
    val dailyHabitsList = homeUiState.habits.filter { it.type == HabitFrequency.DAILY }
    val weeklyHabitsList = homeUiState.habits.filter { it.type == HabitFrequency.WEEKLY }
    // TODO remember filtering operation

    // TODO use further down
    val weekDates = (LocalDate.now().with(DayOfWeek.MONDAY)..LocalDate.now()).toList()
    val dailyCompleted = dailyHabitsList.map { habit ->
        habit.completed.contains(LocalDate.now())
    }.all { it }
    val weeklyCompleted = weeklyHabitsList.map { habit ->
        (habit.completed + habit.completedByWeek).containsAll(weekDates)
    }.all { it }

    LazyColumn(modifier = modifier) {
        this.stickyHeader {
            AnimatedVisibility(
                visible = !dailyCompleted || showCompleted,
                exit = shrinkVertically(animationSpec = TweenSpec(delay = 200)),
                enter = expandVertically()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Text( // TODO could have title area change colour when list is scrolled, e.g timers in google clock
                        text = stringResource(R.string.home_today),
                        style = Typography.titleLarge,
                        modifier = Modifier
                            .padding(start = 30.dp, bottom = 10.dp) // keep inline with habit titles
                            .fillMaxWidth()
                    )
                }
            }
        }
        items(
            items = dailyHabitsList,
            key = { it.id }) { habit ->

            // TODO remember
            val habitCompleted = habit.completed.any { it == LocalDate.now() }

            AnimatedVisibility(
                visible = !habitCompleted || showCompleted,
                exit = shrinkVertically(animationSpec = TweenSpec(delay = 200)),
                enter = expandVertically()
            ) {

                HomeHabitCard(
                    modifier = Modifier
                        .animateItemPlacement(tween())
                        .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                    habit = habit,
                    completedOnClick = completedOnClick,
                    navigateToDetail = navigateToDetail,
                    showStreaks = homeUiState.showStreaks
                )

            }
        }
        item {
            val completedCount = dailyHabitsList.count { habit ->
                habit.completed.any { it == LocalDate.now() }
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
            AnimatedVisibility(
                visible = !weeklyCompleted || showCompleted,
                exit = shrinkVertically(animationSpec = TweenSpec(delay = 200)),
                enter = expandVertically()
            ) {


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Text( // TODO could have title area change colour when list is scrolled, e.g timers in google clock
                        text = "This Week",
                        style = Typography.titleLarge,
                        modifier = Modifier
                            .padding(start = 30.dp, bottom = 10.dp) // keep inline with habit titles
                            .fillMaxWidth()
                    )
                }

            }
        }
        items(
            items = weeklyHabitsList,
            key = { it.id }) { habit ->

            // TODO remember
            val weekDates = (LocalDate.now().with(DayOfWeek.MONDAY)..LocalDate.now()).toList()
            val weekCompleted = (habit.completed + habit.completedByWeek).containsAll(weekDates)

            AnimatedVisibility(
                visible = !weekCompleted || showCompleted,
                exit = shrinkVertically(animationSpec = TweenSpec(delay = 200)),
                enter = expandVertically()
            ) {

                HomeHabitCard(
                    modifier = Modifier
                        .animateItemPlacement(tween())
                        .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                    habit = habit,
                    completedOnClick = completedOnClick,
                    navigateToDetail = navigateToDetail,
                    showStreaks = homeUiState.showStreaks
                )

            }
        }
        item {
            // TODO remember ?
            val weekDates = (LocalDate.now().with(DayOfWeek.MONDAY)..LocalDate.now()).toList()
            val completedCount = weeklyHabitsList.count { habit ->
                (habit.completed + habit.completedByWeek).containsAll(weekDates)
            }
            if (homeUiState.showSubtitle) {
                HabitListSubtitle(
                    modifier = Modifier.fillMaxWidth(),
                    visible = completedCount > 0 && !showCompleted,
                    text = pluralStringResource(
                        id = R.plurals.home_habit_list_weekly_subtitle,
                        count = completedCount,
                        completedCount
                    )
                )
            }
            // Spacer at the bottom ensures that FAB does not obscure habits at the bottom of the list
            Spacer(modifier.height(100.dp)) // TODO
        }
    }

}

@Composable
fun HabitListSubtitle(
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenNoHabitsPreview() {
    HabitsTheme {
        HomeScreen(
            navigateToAddHabit = {},
            navigateToDetail = {},
            navigateToSettings = {},
            navigateToLogbook = {},
            homeUiState = HomeUiState.Empty,
            completedOnClick = { _, _ -> },
        )
    }
}