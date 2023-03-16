package com.willbsp.habits.ui.screens.logbook

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
        logbookCalendarUiState = logbookUiState,
        completedOnClick = { date -> viewModel.toggleEntry(date) },
        navigateUp = navigateUp
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Logbook(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    logbookCalendarUiState: LogbookCalendarUiState,
    completedOnClick: (LocalDate) -> Unit
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

        when (logbookCalendarUiState) {

            is LogbookCalendarUiState.SelectedHabit -> {

                LogbookDatePicker(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    dates = logbookCalendarUiState,
                    selectedHabitId = 3,
                    dateOnClick = completedOnClick
                )

            }

            else -> { // TODO
                Text("this text should not be here!")
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
        logbookCalendarUiState = LogbookCalendarUiState.NoSelection,
        //onSelectedHabitChange = {},
        completedOnClick = { }
    )
}