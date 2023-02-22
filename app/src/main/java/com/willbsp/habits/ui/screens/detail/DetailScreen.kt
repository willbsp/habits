package com.willbsp.habits.ui.screens.detail

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.willbsp.habits.ui.common.DefaultHabitsAppTopBar
import com.willbsp.habits.ui.theme.HabitsTheme

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel,
    navigateUp: () -> Unit,
    navigateToEditHabit: (Int) -> Unit
) {

    Detail(
        modifier = modifier,
        navigateUp = navigateUp,
        navigateToEditHabit = navigateToEditHabit
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Detail(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    navigateToEditHabit: (Int) -> Unit
) {

    Scaffold(
        topBar = {

            DefaultHabitsAppTopBar(
                title = "Flashcards", // TODO
                canNavigateBack = true,
                navigateUp = navigateUp,
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = null // TODO
                        )
                    }
                }
            )

        }
    ) {

        Text(modifier = Modifier.padding(it), text = "temp")

    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetailScreenPreview() {
    HabitsTheme {
        Detail(navigateUp = { /*TODO*/ }, navigateToEditHabit = {})
    }
}