package com.willbsp.habits.ui.screens.edit

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.willbsp.habits.R
import com.willbsp.habits.ui.common.DefaultHabitsAppTopBar
import com.willbsp.habits.ui.common.HabitForm
import com.willbsp.habits.ui.common.HabitUiState
import com.willbsp.habits.ui.common.HabitsFloatingAction
import com.willbsp.habits.ui.theme.HabitsTheme

@Composable
fun EditHabitScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onValueChange: (HabitUiState) -> Unit,
    habitUiState: HabitUiState
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

        val deleteDialogOpen = remember { mutableStateOf(false) }

        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = 10.dp)
                .fillMaxSize()
        ) {

            when (habitUiState) {

                is HabitUiState.Habit -> {

                    HabitForm(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onValueChange = onValueChange,
                        habitUiState = habitUiState
                    )

                    Spacer(Modifier.height(10.dp))

                    FilledTonalButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { deleteDialogOpen.value = true }
                    ) {
                        Text(stringResource(R.string.edit_habit_delete))
                    }

                }

                else -> {}

            }

        }

        if (deleteDialogOpen.value) {
            EditDeleteDialog(
                onDismiss = { deleteDialogOpen.value = false },
                onConfirm = {
                    deleteDialogOpen.value = false
                    onDeleteClick()
                }
            )
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EditHabitScreenPreview() {
    HabitsTheme {
        EditHabitScreen(
            navigateUp = {},
            onSaveClick = {},
            onDeleteClick = {},
            onValueChange = {},
            habitUiState = HabitUiState.Loading
        )
    }
}
