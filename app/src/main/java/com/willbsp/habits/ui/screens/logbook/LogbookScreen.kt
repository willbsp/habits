package com.willbsp.habits.ui.screens.logbook

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.willbsp.habits.R
import java.time.LocalDate

@Composable
fun LogbookScreen(
    modifier: Modifier = Modifier,
    viewModel: LogbookViewModel,
    navigateToHome: () -> Unit,
    navigateToSettings: () -> Unit
) {

    val logbookUiState by viewModel.logbookUiState.collectAsState(LogbookUiState())

    Logbook(
        modifier = modifier,
        navigateToHome = navigateToHome,
        navigateToSettings = navigateToSettings,
        logbookUiState = logbookUiState,
        onSelectedDateChange = { viewModel.setSelectedDate(it) }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Logbook(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    navigateToSettings: () -> Unit,
    logbookUiState: LogbookUiState,
    onSelectedDateChange: (LocalDate) -> Unit
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
                            imageVector = Icons.Filled.Done,
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
    ) { innerPadding -> // TODO

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            DatePickerCard(
                modifier = Modifier
                    .height(70.dp)
                    .fillMaxWidth(),
                onSelectedDateChange = onSelectedDateChange
            )


            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

                items(items = logbookUiState.habits, key = { it.habit.id }) { habit ->

                    Text(habit.habit.name + " " + habit.completed.toString())

                }

            }

        }

    }
}

@Preview
@Composable
fun LogbookPreview() {
    Logbook(
        navigateToSettings = {},
        navigateToHome = {},
        logbookUiState = LogbookUiState(),
        onSelectedDateChange = {}
    )
}