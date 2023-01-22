package com.willbsp.habits.ui.screens.edit

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.willbsp.habits.R
import com.willbsp.habits.ui.screens.common.DefaultHabitsAppTopBar
import com.willbsp.habits.ui.screens.common.HabitsFloatingAction
import com.willbsp.habits.ui.screens.common.ModifyHabitForm
import com.willbsp.habits.ui.screens.common.ModifyHabitUiState
import com.willbsp.habits.ui.theme.HabitsTheme
import kotlinx.coroutines.launch

@Composable
fun EditHabitScreen(
    modifier: Modifier = Modifier,
    viewModel: EditHabitViewModel,
    navigateUp: () -> Unit,
    navigateBack: () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()

    EditHabit(
        modifier = modifier,
        navigateUp = navigateUp,
        onSaveClick = {
            coroutineScope.launch {
                viewModel.updateHabit()
            }
            navigateBack()
        },
        onDeleteClick = {
            coroutineScope.launch {
                viewModel.deleteHabit()
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
private fun EditHabit(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onValueChange: (ModifyHabitUiState) -> Unit,
    habitUiState: ModifyHabitUiState
) {

    Scaffold(
        topBar = {
            DefaultHabitsAppTopBar(
                title = stringResource(R.string.edit_habit_title),
                canNavigateBack = true,
                navigateUp = navigateUp
            )
        },
        floatingActionButton = {
            HabitsFloatingAction(
                onClick = onSaveClick,
                icon = Icons.Default.Done,
                contentDescription = stringResource(R.string.edit_habit_update_habit)
            )
        }
    ) { innerPadding ->

        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = 10.dp)
                .fillMaxSize()
        ) {

            ModifyHabitForm(
                modifier = Modifier
                    .fillMaxWidth(),
                onValueChange = onValueChange,
                habitUiState = habitUiState
            )

            Spacer(Modifier.height(10.dp))

            FilledTonalButton( // TODO need a confirmation dialog for delete
                modifier = Modifier.fillMaxWidth(),
                onClick = onDeleteClick
            ) {
                Text(stringResource(R.string.edit_habit_delete))
            }

        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EditHabitScreenPreview() {
    HabitsTheme {
        EditHabit(
            navigateUp = {},
            onSaveClick = {},
            onDeleteClick = {},
            onValueChange = {},
            habitUiState = ModifyHabitUiState()
        )
    }
}
