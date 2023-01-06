package com.willbsp.habits.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.willbsp.habits.R
import com.willbsp.habits.data.Habit
import com.willbsp.habits.data.HabitFrequency
import com.willbsp.habits.di.AppViewModelProvider
import com.willbsp.habits.ui.HabitsAppTopBar
import com.willbsp.habits.ui.theme.HabitsTheme
import com.willbsp.habits.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToAddHabit: () -> Unit = {},
    viewModel: HomeScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    habitsList: List<Habit> = listOf() // TODO temp for building
) {

    val homeUiState by viewModel.homeUiState.collectAsState()

    Scaffold(
        topBar = {
            HabitsAppTopBar(
                title = stringResource(R.string.app_name),
                canNavigateBack = false
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

        HabitsList(
            //habitsList = habitsList,
            habitsList = homeUiState.habitsList,
            modifier = Modifier
                .padding(innerPadding)
                .padding(10.dp)
        )

    }

}

@Composable
private fun HabitsList( // TODO go through all composable files and make any private
    habitsList: List<Habit>,
    modifier: Modifier = Modifier
) {

    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(items = habitsList, key = { it.id }) { habit ->
            HabitCard(habit = habit)
        }
    }

}

@Composable
private fun HabitCard(
    habit: Habit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(50.dp), // TODO not needed
        elevation = CardDefaults.cardElevation() // TODO is there a default relating to the theme
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = habit.name, style = Typography.titleLarge)
            Spacer(modifier = Modifier.weight(1f))
            //if (habit.completed) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = stringResource(R.string.home_screen_completed)
                    )
                }
            /*} else {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.home_screen_uncompleted)
                    )
                }
            }*/
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HabitsTheme() {
        HabitsList(
            habitsList = listOf(
                Habit(id = 0, name = "Running", frequency = HabitFrequency.DAILY),
                Habit(id = 1, name = "Programming", frequency = HabitFrequency.DAILY)
            )
        )
    }
}