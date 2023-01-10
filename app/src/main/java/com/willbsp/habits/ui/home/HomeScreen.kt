package com.willbsp.habits.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.willbsp.habits.R
import com.willbsp.habits.data.model.Habit
import com.willbsp.habits.di.AppViewModelProvider
import com.willbsp.habits.ui.HabitsAppTopBar
import com.willbsp.habits.ui.theme.HabitsTheme
import com.willbsp.habits.ui.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToAddHabit: () -> Unit,
    viewModel: HomeScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val homeUiState by viewModel.homeUiState.collectAsState(HomeUiState())
    val coroutineScope = rememberCoroutineScope()

    Home(
        modifier = modifier,
        navigateToAddHabit = navigateToAddHabit,
        buttonOnClick = { habit ->
            coroutineScope.launch {
                viewModel.toggleEntry(habit)
            }
        },
        homeUiState = homeUiState
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Home(
    modifier: Modifier = Modifier,
    buttonOnClick: (Habit) -> Unit,
    navigateToAddHabit: () -> Unit,
    homeUiState: HomeUiState
) {

    Scaffold(
        topBar = {
            HabitsAppTopBar(
                title = stringResource(R.string.app_name),
                canNavigateBack = false,
                navigateToSettings = {}
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToAddHabit,
                shape = CircleShape,
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.home_screen_add_habit)
                )
            }
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
                habitUiStateList = homeUiState.habitUiState,
                buttonOnClick = buttonOnClick,
                modifier = Modifier
            )

        }


    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HabitsList(
    habitUiStateList: List<HomeHabitUiState>,
    buttonOnClick: (Habit) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(items = habitUiStateList, key = { it.habit.id }) { habit ->
            HabitCard(habitUiState = habit, buttonOnClick = buttonOnClick)
        }
        // Spacer at the bottom ensures that FAB does not obscure habits at the bottom of the list
        this.stickyHeader { Spacer(modifier.height(100.dp)) }
    }

    Spacer(modifier = Modifier.height(50.dp))

}

@Composable
private fun HabitCard(
    habitUiState: HomeHabitUiState,
    buttonOnClick: (Habit) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.height(70.dp),
        colors = CardDefaults.cardColors()
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = habitUiState.habit.name,
                style = Typography.titleLarge,
            )

            Spacer(modifier = Modifier.weight(1f))

            IconToggleButton(
                onCheckedChange = {
                    buttonOnClick(habitUiState.habit)
                },
                checked = habitUiState.completed,
                colors = IconButtonDefaults.filledIconToggleButtonColors()
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = stringResource(R.string.home_screen_completed)
                )
            }

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    HabitsTheme {
        Home(
            navigateToAddHabit = {},
            homeUiState = HomeUiState(
                /*listOf(
                    Habit(
                        id = 0, name = "Running", frequency = HabitFrequency.DAILY
                    ),
                    Habit(
                        id = 1, name = "Swimming", frequency = HabitFrequency.WEEKLY
                    ),
                    Habit(
                        id = 2, name = "Reading", frequency = HabitFrequency.DAILY
                    ),
                    Habit(
                        id = 3, name = "Piano Practice", frequency = HabitFrequency.DAILY
                    ),
                )*/
            ),
            buttonOnClick = {}
        )
    }
}