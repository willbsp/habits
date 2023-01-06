package com.willbsp.habits.ui.home

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.willbsp.habits.ui.HabitsAppTopBar
import com.willbsp.habits.ui.theme.HabitsTheme
import com.willbsp.habits.R

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
                    contentDescription = "Add item" // TODO strings.xml
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
        HomeScreen() {}
    }
}