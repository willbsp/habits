package com.willbsp.habits.ui.screens.detail

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.willbsp.habits.R
import com.willbsp.habits.ui.common.DefaultHabitsAppTopBar
import com.willbsp.habits.ui.theme.HabitsTheme
import com.willbsp.habits.ui.theme.Typography

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
                            contentDescription = stringResource(R.string.detail_edit_habit)
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            DetailScoreCard(
                modifier = modifier
                    .width(200.dp)
                    .height(150.dp),
                title = R.string.detail_streak,
                value = detailUiState.streak,
                percentage = false
            )

            DetailScoreCard(
                modifier = modifier
                    .width(200.dp)
                    .height(150.dp),
                title = R.string.detail_score,
                value = detailUiState.score,
                percentage = true
            )

        }

    }

}

@Composable
fun DetailScoreCard(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    value: Int,
    percentage: Boolean
) {

    ElevatedCard(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight(),
            text = "${stringResource(id = title)}\n" +
                    if (percentage) "${value}%" else "$value",
            style = Typography.displayMedium,
            textAlign = TextAlign.Center
        )
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetailScreenPreview() {
    HabitsTheme {
        Detail(
            detailUiState = DetailUiState(-1, "Flashcards", 5, 23),
            navigateUp = { },
            navigateToEditHabit = {}
        )
    }
}