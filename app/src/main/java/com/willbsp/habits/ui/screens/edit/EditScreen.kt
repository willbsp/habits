package com.willbsp.habits.ui.screens.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.willbsp.habits.ui.common.button.HabitsFloatingAction
import com.willbsp.habits.ui.common.form.HabitForm
import com.willbsp.habits.ui.common.form.HabitFormUiState
import com.willbsp.habits.ui.theme.HabitsTheme

@Composable
fun EditScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onValueChange: (HabitFormUiState) -> Unit,
    habitFormUiState: HabitFormUiState
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

            when (habitFormUiState) {

                is HabitFormUiState.HabitData -> {

                    HabitForm(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onValueChange = onValueChange,
                        showTimePicker = {},
                        habitFormUiState = habitFormUiState
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
private fun EditScreenPreview() {
    HabitsTheme {
        EditScreen(
            navigateUp = {},
            onSaveClick = {},
            onDeleteClick = {},
            onValueChange = {},
            habitFormUiState = HabitFormUiState.Loading
        )
    }
}
