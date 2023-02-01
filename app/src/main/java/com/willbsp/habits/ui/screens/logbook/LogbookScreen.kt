package com.willbsp.habits.ui.screens.logbook

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.willbsp.habits.R
import com.willbsp.habits.ui.common.HabitToggleButton
import com.willbsp.habits.ui.theme.Typography
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun LogbookScreen(
    modifier: Modifier = Modifier,
    viewModel: LogbookViewModel,
    navigateToHome: () -> Unit,
    navigateToSettings: () -> Unit
) {

    val logbookUiState by viewModel.logbookUiState.collectAsState(LogbookUiState())
    val coroutineScope = rememberCoroutineScope()

    Logbook(
        modifier = modifier,
        navigateToHome = navigateToHome,
        navigateToSettings = navigateToSettings,
        logbookUiState = logbookUiState,
        onSelectedDateChange = { viewModel.setSelectedDate(it) },
        completedOnClick = { habitId, date ->
            coroutineScope.launch {
                viewModel.toggleEntry(habitId, date)
            }
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Logbook(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    navigateToSettings: () -> Unit,
    logbookUiState: LogbookUiState,
    onSelectedDateChange: (LocalDate) -> Unit,
    completedOnClick: (Int, LocalDate) -> Unit
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.logbook_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                modifier = modifier,
                navigationIcon = {
                    IconButton(onClick = navigateToHome) {
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
        }
    ) { innerPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            var selectedDate by remember { mutableStateOf(LocalDate.now()) }


            LogbookDatePicker(
                modifier = Modifier
                    //.padding(8.dp)
                    .height(110.dp)
                    .fillMaxWidth(),
                onSelectedDateChange = {
                    selectedDate = it
                    onSelectedDateChange(it)
                },
                selectedDate = selectedDate
            )

            Spacer(modifier = Modifier.height(10.dp))

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

            }

        }

    }
}

@Composable
fun LogbookHabitCard(
    modifier: Modifier,
    logbookHabitUiState: LogbookHabitUiState,
    completedOnClick: (Int) -> Unit,
) {

    ElevatedCard(modifier) {

        Row(
            modifier = Modifier
                .height(60.dp)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = logbookHabitUiState.name,
                style = Typography.titleLarge
            )

            Spacer(Modifier.weight(1f))

            HabitToggleButton(
                onCheckedChange = {
                    completedOnClick(logbookHabitUiState.id)
                },
                checked = logbookHabitUiState.completed
            )

        }

    }

}

@Preview
@Composable
fun LogbookPreview() {
    Logbook(
        navigateToSettings = {},
        navigateToHome = {},
        logbookUiState = LogbookUiState(
            listOf(
                LogbookHabitUiState(0, "Running", true),
                LogbookHabitUiState(1, "Flashcards", false),
            )
        ),
        onSelectedDateChange = {},
        completedOnClick = { _, _ -> }
    )
}