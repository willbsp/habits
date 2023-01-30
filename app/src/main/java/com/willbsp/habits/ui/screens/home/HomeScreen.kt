package com.willbsp.habits.ui.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.willbsp.habits.R
import com.willbsp.habits.ui.common.HabitsFloatingAction
import com.willbsp.habits.ui.theme.HabitsTheme
import com.willbsp.habits.ui.theme.Typography
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    navigateToLogbook: () -> Unit,
    navigateToAddHabit: () -> Unit,
    navigateToEditHabit: (Int) -> Unit,
    navigateToSettings: () -> Unit
) {

    val homeUiState by viewModel.homeUiState.collectAsState(HomeUiState())
    val coroutineScope = rememberCoroutineScope()

    Home(
        modifier = modifier,
        navigateToLogbook = navigateToLogbook,
        navigateToAddHabit = navigateToAddHabit,
        navigateToEditHabit = navigateToEditHabit,
        navigateToSettings = navigateToSettings,
        completedOnClick = { id, date ->
            coroutineScope.launch {
                viewModel.toggleEntry(id, date)
            }
        },
        homeUiState = homeUiState
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Home(
    modifier: Modifier = Modifier,
    completedOnClick: (Int, LocalDate) -> Unit,
    navigateToLogbook: () -> Unit,
    navigateToAddHabit: () -> Unit,
    navigateToEditHabit: (Int) -> Unit,
    navigateToSettings: () -> Unit,
    homeUiState: HomeUiState
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        fontWeight = FontWeight.Bold
                    )
                },
                modifier = modifier,
                navigationIcon = {
                    IconButton(onClick = navigateToLogbook) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = ""
                        )
                    }
                },
                actions = {
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
                habitUiStateList = homeUiState.todayState,
                completedOnClick = completedOnClick,
                navigateToEditHabit = navigateToEditHabit,
                modifier = Modifier
            )

        }


    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HabitsList(
    habitUiStateList: List<HomeHabitUiState>,
    completedOnClick: (Int, LocalDate) -> Unit,
    navigateToEditHabit: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(items = habitUiStateList, key = { it.id }) { habit ->
            HomeHabitCard(
                habitUiState = habit,
                completedOnClick = completedOnClick,
                navigateToEditHabit = navigateToEditHabit
            )
        }
        // Spacer at the bottom ensures that FAB does not obscure habits at the bottom of the list
        this.stickyHeader { Spacer(modifier.height(100.dp)) }
    }

    Spacer(modifier = Modifier.height(50.dp))

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    HabitsTheme {
        Home(
            navigateToAddHabit = {},
            navigateToEditHabit = {},
            navigateToSettings = {},
            navigateToLogbook = {},
            homeUiState = HomeUiState(
                listOf(
                    HomeHabitUiState(
                        id = 0,
                        name = "Running",
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
                        completedDates = listOf(
                            HomeCompletedUiState(LocalDate.parse("2023-04-12"), false),
                            HomeCompletedUiState(LocalDate.parse("2023-04-11"), true),
                            HomeCompletedUiState(LocalDate.parse("2023-04-10"), false),
                            HomeCompletedUiState(LocalDate.parse("2023-04-09"), true),
                            HomeCompletedUiState(LocalDate.parse("2023-04-08"), false),
                            HomeCompletedUiState(LocalDate.parse("2023-04-07"), true),
                        )
                    ),
                )
            ),
            completedOnClick = { _, _ ->

            }
        )
    }
}