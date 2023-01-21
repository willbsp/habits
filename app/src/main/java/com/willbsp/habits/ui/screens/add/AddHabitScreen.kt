package com.willbsp.habits.ui.screens.add

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.willbsp.habits.R
import com.willbsp.habits.ui.HabitsAppTopBar
import com.willbsp.habits.ui.screens.common.HabitsFloatingAction
import com.willbsp.habits.ui.screens.common.ModifyHabitForm
import com.willbsp.habits.ui.screens.common.ModifyHabitUiState
import com.willbsp.habits.ui.theme.HabitsTheme
import kotlinx.coroutines.launch

@Composable
fun AddHabitScreen(
    modifier: Modifier = Modifier,
    viewModel: AddHabitViewModel,
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
            }
            navigateBack()
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
    onValueChange: (ModifyHabitUiState) -> Unit,
    habitUiState: ModifyHabitUiState
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
            HabitsFloatingAction(
                onClick = onSaveClick,
                icon = Icons.Default.Done,
                contentDescription = stringResource(R.string.add_habit_add_habit)
            )
        }
    ) { innerPadding ->

        ModifyHabitForm(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = 10.dp)
                .fillMaxSize(),
            onValueChange = onValueChange,
            habitUiState = habitUiState
        )

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddHabitScreenPreview() {
    HabitsTheme {
        AddHabit(
            navigateUp = {},
            onSaveClick = {},
            onValueChange = {},
            habitUiState = ModifyHabitUiState()
        )
    }
}
