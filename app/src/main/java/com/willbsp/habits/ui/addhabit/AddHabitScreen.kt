package com.willbsp.habits.ui.addhabit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.willbsp.habits.R
import com.willbsp.habits.di.AppViewModelProvider
import com.willbsp.habits.ui.HabitsAppTopBar
import com.willbsp.habits.ui.theme.HabitsTheme
import kotlinx.coroutines.launch

// TODO change the package name?

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen( // TODO break down into multiple composable
    modifier: Modifier = Modifier,
    popToHome: () -> Unit = {},
    viewModel: AddHabitViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            HabitsAppTopBar(
                title = stringResource(R.string.add_habit_title),
                canNavigateBack = true,
                navigateUp = popToHome
            )
        },
        floatingActionButton = {
            FloatingActionButton( // TODO create composable
                onClick = {
                    coroutineScope.launch {
                        viewModel.saveHabit()
                        popToHome()
                    }
                },
                shape = CircleShape,
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = stringResource(R.string.add_habit_add_habit)
                )
            }
        }
    ) { innerPadding -> // TODO

        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = viewModel.habitUiState.name,
                singleLine = true, // TODO validation needed
                onValueChange = { viewModel.updateUiState(viewModel.habitUiState.copy(name = it)) }, // TODO stored and read from uistate
                label = { Text(stringResource(R.string.add_habit_name)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            )


            OutlinedTextField(
                value = "",
                onValueChange = { /*TODO*/ }, // TODO stored and read from uistate
                label = { Text(stringResource(R.string.add_habit_frequency)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            )

        }

    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddHabitScreenPreview() {
    HabitsTheme() {
        AddHabitScreen()
    }
}
