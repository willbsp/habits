package com.willbsp.habits.ui.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.willbsp.habits.R
import com.willbsp.habits.ui.HabitsAppTopBar
import com.willbsp.habits.ui.theme.HabitsTheme
import com.willbsp.habits.ui.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel,
    navigateToAddHabit: () -> Unit,
    navigateToEditHabit: (Int) -> Unit,
    navigateToSettings: () -> Unit
) {

    val homeUiState by viewModel.homeUiState.collectAsState(HomeUiState())
    val coroutineScope = rememberCoroutineScope()

    Home(
        modifier = modifier,
        navigateToAddHabit = navigateToAddHabit,
        navigateToEditHabit = navigateToEditHabit,
        navigateToSettings = navigateToSettings,
        buttonOnClick = { id ->
            coroutineScope.launch {
                viewModel.toggleEntry(id)
            }
        },
        homeUiState = homeUiState
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Home(
    modifier: Modifier = Modifier,
    buttonOnClick: (Int) -> Unit,
    navigateToAddHabit: () -> Unit,
    navigateToEditHabit: (Int) -> Unit,
    navigateToSettings: () -> Unit,
    homeUiState: HomeUiState
) {


    Scaffold(
        topBar = {
            HabitsAppTopBar(
                title = stringResource(R.string.app_name),
                canNavigateBack = false,
                navigateToSettings = navigateToSettings
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
                habitUiStateList = homeUiState.state,
                buttonOnClick = buttonOnClick,
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
    buttonOnClick: (Int) -> Unit,
    navigateToEditHabit: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(items = habitUiStateList, key = { it.id }) { habit ->
            HabitCard(
                habitUiState = habit,
                completedOnClick = buttonOnClick,
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
            homeUiState = HomeUiState(
                listOf(
                    HomeHabitUiState(
                        id = 0,
                        name = "Running",
                        completed = false
                    ),
                    HomeHabitUiState(
                        id = 1,
                        name = "Swimming",
                        completed = true
                    ),
                    HomeHabitUiState(
                        id = 2,
                        name = "Reading",
                        completed = false
                    ),
                    HomeHabitUiState(
                        id = 3,
                        name = "Piano Practice",
                        completed = true
                    ),
                )
            ),
            buttonOnClick = {}
        )
    }
}