package com.willbsp.habits.ui.screens.logbook

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.willbsp.habits.R
import com.willbsp.habits.ui.common.DefaultHabitsAppTopBar
import java.time.LocalDate

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun LogbookScreen(
    modifier: Modifier = Modifier,
    viewModel: LogbookViewModel,
    navigateUp: () -> Unit,
) {

    val logbookUiState by viewModel.uiState.collectAsStateWithLifecycle()

    Logbook(
        modifier = modifier,
        logbookUiState = logbookUiState,
        completedOnClick = { date -> viewModel.toggleEntry(date) },
        habitOnClick = { habitId -> viewModel.setSelectedHabit(habitId) },
        navigateUp = navigateUp
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Logbook(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    logbookUiState: LogbookUiState,
    completedOnClick: (LocalDate) -> Unit,
    habitOnClick: (Int) -> Unit
) {

    Scaffold(
        topBar = {
            DefaultHabitsAppTopBar(
                title = stringResource(R.string.logbook_title),
                canNavigateBack = true,
                navigateUp = navigateUp
            )
        },
    ) { innerPadding ->

        when (logbookUiState) {

            is LogbookUiState.SelectedHabit -> {

                Column(
                    modifier = modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom
                ) {


                    LogbookDatePicker(
                        modifier = Modifier.weight(1f, false),
                        dates = logbookUiState.selectedHabitDates,
                        selectedHabitId = 3,
                        dateOnClick = completedOnClick
                    )

                    Divider()

                    LazyRow(
                        modifier = Modifier
                            .height(60.dp)
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(items = logbookUiState.habits, key = { it.id }) {
                            FilterChip(
                                modifier = Modifier
                                    .padding(end = 8.dp),
                                selected = it.id == logbookUiState.selectedHabitId,
                                onClick = { habitOnClick(it.id) },
                                label = { Text(text = it.name) })
                        }
                    }

                }

            }

            else -> { // TODO
                Text(
                    modifier = modifier.padding(innerPadding),
                    text = "this text should not be here!"
                )
            }

        }

        /*Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(items = logbookUiState.habits, key = { it.id }) { habitUiState ->

                LogbookHabitCard(
                    modifier = Modifier.fillMaxWidth(),
                    logbookHabitUiState = habitUiState,
                    completedOnClick = {
                        completedOnClick(it, selectedDate)
                    }
                )

            }

        }*/

        //}

    }
}

@Preview
@Composable
fun LogbookPreview() {
    Logbook(
        navigateUp = {},
        logbookUiState = LogbookUiState.SelectedHabit(
            listOf(
                LogbookUiState.Habit(id = 0, name = "Running"),
                LogbookUiState.Habit(id = 1, name = "Flashcards"),
                LogbookUiState.Habit(id = 2, name = "Reading"),
                LogbookUiState.Habit(id = 3, name = "Meditation"),
            ), 0, listOf()
        ),
        completedOnClick = { },
        habitOnClick = { }
    )
}