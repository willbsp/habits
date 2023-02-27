package com.willbsp.habits.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.willbsp.habits.R
import com.willbsp.habits.ui.common.HabitsFloatingAction
import com.willbsp.habits.ui.common.PreferencesUiState
import com.willbsp.habits.ui.theme.HabitsTheme
import com.willbsp.habits.ui.theme.Typography
import java.time.LocalDate

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    navigateToLogbook: () -> Unit,
    navigateToAddHabit: () -> Unit,
    navigateToDetail: (Int) -> Unit,
    navigateToSettings: () -> Unit
) {

    val homeUiState by viewModel.homeUiState.collectAsStateWithLifecycle(HomeUiState())
    val preferencesState by viewModel.preferencesUiState.collectAsStateWithLifecycle()

    Home(
        modifier = modifier,
        navigateToLogbook = navigateToLogbook,
        navigateToAddHabit = navigateToAddHabit,
        navigateToDetail = navigateToDetail,
        navigateToSettings = navigateToSettings,
        completedOnClick = { id, date -> viewModel.toggleEntry(id, date) },
        homeUiState = homeUiState,
        preferencesUiState = preferencesState
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Home(
    modifier: Modifier = Modifier,
    completedOnClick: (Int, LocalDate) -> Unit,
    navigateToLogbook: () -> Unit,
    navigateToAddHabit: () -> Unit,
    navigateToDetail: (Int) -> Unit,
    navigateToSettings: () -> Unit,
    preferencesUiState: PreferencesUiState,
    homeUiState: HomeUiState
) {

    var showCompleted by remember { mutableStateOf(true) }

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
                    }) { // TODO tooltip for show completed
                        Icon(
                            imageVector = if (showCompleted) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = null // TODO
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
                contentDescription = stringResource(R.string.home_screen_add_habit)
            )
        }
    ) { innerPadding -> // TODO

        AnimatedVisibility(
            visible = !homeUiState.allCompleted || showCompleted,
            enter = fadeIn(),
            exit = fadeOut(
                tween(durationMillis = 500)
            )
        ) {

            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
                    .fillMaxSize()
            ) {


                Text( // TODO could have title area change colour when list is scrolled, e.g timers in google clock
                    text = stringResource(R.string.home_screen_today),
                    style = Typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 10.dp) // keep inline with habit titles
                )

                Spacer(modifier = Modifier.height(10.dp))

                HabitsList(
                    homeUiState = homeUiState,
                    completedOnClick = completedOnClick,
                    navigateToDetail = navigateToDetail,
                    showStreaks = preferencesUiState.showStreaks,
                    showSubtitle = preferencesUiState.showCompletedSubtitle,
                    showCompleted = showCompleted
                )

            }

        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = homeUiState.allCompleted && !showCompleted,
                enter = expandVertically(
                    tween(delayMillis = 1000)
                ),
                exit = fadeOut()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Icon(
                        modifier = Modifier.size(100.dp),
                        imageVector = Icons.Default.Done,
                        contentDescription = stringResource(R.string.home_screen_all_completed_tick)
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = stringResource(R.string.home_screen_all_completed),
                        style = Typography.titleLarge
                    )

                }

            }
        }


    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HabitsList(
    homeUiState: HomeUiState,
    completedOnClick: (Int, LocalDate) -> Unit,
    navigateToDetail: (Int) -> Unit,
    showStreaks: Boolean,
    showSubtitle: Boolean,
    showCompleted: Boolean,
    modifier: Modifier = Modifier
) {

    val habitUiStateList = homeUiState.todayState

    LazyColumn(modifier = modifier) {
        items(items = habitUiStateList, key = { it.id }) { homeHabitUiState ->
            AnimatedVisibility(
                visible = !homeHabitUiState.completedDates.first().completed || showCompleted,
                exit = shrinkVertically(
                    animationSpec = TweenSpec(
                        delay = 500
                    ),
                ),
                enter = expandVertically()
            ) {

                HomeHabitCard(
                    modifier = Modifier
                        .animateItemPlacement(tween())
                        .padding(bottom = 10.dp),
                    habitUiState = homeHabitUiState,
                    completedOnClick = completedOnClick,
                    navigateToDetail = navigateToDetail,
                    showStreaks = showStreaks
                )

            }
        }
        this.stickyHeader {
            if (showSubtitle) {
                AnimatedVisibility(
                    visible = homeUiState.completedCount > 0 && !showCompleted,
                    enter = fadeIn(
                        animationSpec = TweenSpec(
                            delay = 1000
                        )
                    ),
                    exit = fadeOut()
                ) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(
                                R.string.home_screen_habit_list_subtitle,
                                homeUiState.completedCount
                            ),
                            style = Typography.labelLarge
                        )
                    }
                }
            }
            // Spacer at the bottom ensures that FAB does not obscure habits at the bottom of the list
            Spacer(modifier.height(100.dp))
        }
    }

    Spacer(modifier = Modifier.height(50.dp))

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    HabitsTheme {
        Home(
            navigateToAddHabit = {},
            navigateToDetail = {},
            navigateToSettings = {},
            navigateToLogbook = {},
            homeUiState = HomeUiState(
                listOf(
                    HomeHabitUiState(
                        id = 0,
                        name = "Running",
                        streak = 2,
                        completedDates = listOf(
                            HomeCompletedUiState(LocalDate.parse("2023-04-12"), false),
                            HomeCompletedUiState(LocalDate.parse("2023-04-11"), true),
                            HomeCompletedUiState(LocalDate.parse("2023-04-10"), false),
                            HomeCompletedUiState(LocalDate.parse("2023-04-09"), true),
                            HomeCompletedUiState(LocalDate.parse("2023-04-08"), false),
                            HomeCompletedUiState(LocalDate.parse("2023-04-07"), true),
                        )
                    ),
                    HomeHabitUiState(
                        id = 1,
                        name = "Swimming",
                        streak = 4,
                        completedDates = listOf(
                            HomeCompletedUiState(LocalDate.parse("2023-04-12"), true),
                            HomeCompletedUiState(LocalDate.parse("2023-04-11"), true),
                            HomeCompletedUiState(LocalDate.parse("2023-04-10"), false),
                            HomeCompletedUiState(LocalDate.parse("2023-04-09"), true),
                            HomeCompletedUiState(LocalDate.parse("2023-04-08"), false),
                            HomeCompletedUiState(LocalDate.parse("2023-04-07"), true),
                        )
                    ),
                    HomeHabitUiState(
                        id = 2,
                        name = "Reading",
                        streak = 5,
                        completedDates = listOf(
                            HomeCompletedUiState(LocalDate.parse("2023-04-12"), false),
                            HomeCompletedUiState(LocalDate.parse("2023-04-11"), true),
                            HomeCompletedUiState(LocalDate.parse("2023-04-10"), false),
                            HomeCompletedUiState(LocalDate.parse("2023-04-09"), true),
                            HomeCompletedUiState(LocalDate.parse("2023-04-08"), false),
                            HomeCompletedUiState(LocalDate.parse("2023-04-07"), true),
                        )
                    ),
                    HomeHabitUiState(
                        id = 3,
                        name = "Piano Practice",
                        streak = 0,
                        completedDates = listOf(
                            HomeCompletedUiState(LocalDate.parse("2023-04-12"), false),
                            HomeCompletedUiState(LocalDate.parse("2023-04-11"), true),
                            HomeCompletedUiState(LocalDate.parse("2023-04-10"), false),
                            HomeCompletedUiState(LocalDate.parse("2023-04-09"), true),
                            HomeCompletedUiState(LocalDate.parse("2023-04-08"), false),
                            HomeCompletedUiState(LocalDate.parse("2023-04-07"), true),
                        )
                    ),
                ),
                //allCompleted = true
            ),
            completedOnClick = { _, _ ->

            },
            preferencesUiState = PreferencesUiState()
        )
    }
}