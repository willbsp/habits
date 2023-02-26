package com.willbsp.habits.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.willbsp.habits.ui.common.DefaultHabitsAppTopBar
import com.willbsp.habits.ui.theme.HabitsTheme
import com.willbsp.habits.ui.theme.Typography

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel,
    navigateUp: () -> Unit,
    navigateToEditHabit: (Int) -> Unit
) {

    val detailUiState by viewModel.detailUiState.collectAsStateWithLifecycle(DetailUiState(-1, ""))

    Detail(
        modifier = modifier,
        detailUiState = detailUiState,
        navigateUp = navigateUp,
        navigateToEditHabit = navigateToEditHabit
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Detail(
    modifier: Modifier = Modifier,
    detailUiState: DetailUiState,
    navigateUp: () -> Unit,
    navigateToEditHabit: (Int) -> Unit
) {

    Scaffold(
        topBar = {

            DefaultHabitsAppTopBar(
                title = detailUiState.habitName,
                canNavigateBack = true,
                navigateUp = navigateUp,
                actions = {
                    IconButton(onClick = { navigateToEditHabit(detailUiState.habitId) }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = null // TODO
                        )
                    }
                }
            )

        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Streak",
                style = Typography.displaySmall
            )
            Text(
                text = detailUiState.streak.toString(),
                style = Typography.displayLarge
            )

            Spacer(modifier.height(30.dp))

            Text(
                text = "Score",
                style = Typography.displaySmall
            )
            Text(
                text = "${detailUiState.score}%",
                style = Typography.displayLarge
            )

        }

    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetailScreenPreview() {
    HabitsTheme {
        Detail(
            detailUiState = DetailUiState(-1, "Flashcards", 5, 23),
            navigateUp = { /*TODO*/ },
            navigateToEditHabit = {}
        )
    }
}