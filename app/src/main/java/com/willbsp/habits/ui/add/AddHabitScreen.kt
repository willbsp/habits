package com.willbsp.habits.ui.add

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

@Composable
fun AddHabitScreen(
    modifier: Modifier = Modifier,
    viewModel: AddHabitViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateUp: () -> Unit,
    navigateBack: () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()

    AddHabit(
        modifier = modifier,
        navigateUp = navigateUp,
        onSaveClick = {
            coroutineScope.launch {
                viewModel.saveHabit()
                navigateBack()
            }
        },
        onValueChange = {
            viewModel.updateUiState(it)
        },
        habitUiState = viewModel.habitUiState
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddHabit(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    onSaveClick: () -> Unit,
    onValueChange: (HabitUiState) -> Unit,
    habitUiState: HabitUiState
) {

    Scaffold(
        topBar = {
            HabitsAppTopBar(
                title = stringResource(R.string.add_habit_title),
                canNavigateBack = true,
                navigateUp = navigateUp
            )
        },
        floatingActionButton = {
            FloatingActionButton( // TODO create composable
                onClick = onSaveClick,
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

        AddHabitForm(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = 10.dp)
                .fillMaxSize(),
            onValueChange = onValueChange,
            habitUiState = habitUiState
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddHabitForm(
    modifier: Modifier = Modifier,
    onValueChange: (HabitUiState) -> Unit,
    habitUiState: HabitUiState
) {

    Column(
        modifier = modifier.fillMaxSize(), // TODO padding somewhere higher level
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = habitUiState.name,
            singleLine = true,
            onValueChange = { onValueChange(habitUiState.copy(name = it)) },
            label = { Text(stringResource(R.string.add_habit_name)) },
            modifier = Modifier.fillMaxWidth()
        )

    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddHabitScreenPreview() {
    HabitsTheme() {
        AddHabit(
            navigateUp = {},
            onSaveClick = {},
            onValueChange = {},
            habitUiState = HabitUiState()
        )
    }
}
