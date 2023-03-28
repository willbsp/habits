package com.willbsp.habits.ui.screens.detail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.willbsp.habits.R
import com.willbsp.habits.ui.common.DefaultHabitsAppTopBar
import com.willbsp.habits.ui.theme.HabitsTheme
import com.willbsp.habits.ui.theme.Typography
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel,
    navigateUp: () -> Unit,
    navigateToEditHabit: (Int) -> Unit
) {

    val detailUiState by viewModel.detailUiState.collectAsStateWithLifecycle()

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
    ) { innerPadding ->

        Column(
            modifier = Modifier
                //.verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(20.dp)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {


            CircularDetailScoreCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                score = detailUiState.score.toFloat()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                DetailCard(
                    modifier = modifier.weight(1f),
                    title = stringResource(R.string.detail_streak),
                    value = detailUiState.streak.toString()
                )

                Spacer(modifier.width(20.dp))

                DetailCard(
                    modifier = modifier.weight(1f),
                    title = stringResource(R.string.detail_longest_streak),
                    value = detailUiState.longestStreak.toString()
                )

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                val startedDate =
                    if (detailUiState.started == null) stringResource(R.string.detail_not_started)
                    else {
                        "${detailUiState.started.dayOfMonth} ${
                            detailUiState.started.month.getDisplayName(
                                TextStyle.SHORT,
                                Locale.getDefault()
                            )
                        }"
                    }

                DetailCard(
                    modifier = modifier.weight(1f),
                    title = stringResource(R.string.detail_started),
                    value = startedDate
                )

                Spacer(modifier.width(20.dp))

                DetailCard(
                    modifier = modifier.weight(1f),
                    title = stringResource(R.string.detail_total),
                    value = detailUiState.total.toString()
                )

            }


        }

    }

}

@Composable
fun CircularDetailScoreCard(
    modifier: Modifier = Modifier,
    score: Float,
) {

    var initialScore by rememberSaveable {
        mutableStateOf(0f)
    }

    val animatedScore = animateFloatAsState(
        targetValue = initialScore,
        animationSpec = tween(durationMillis = 1000)
    )

    LaunchedEffect(score) {
        initialScore = score
    }

    OutlinedCard(
        modifier = modifier,
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            val foregroundColor = MaterialTheme.colorScheme.primary
            val inactiveColor = MaterialTheme.colorScheme.primaryContainer
            val lineThickness = 20.dp
            val lineThicknessInactive = 10.dp

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(25.dp)
            ) {

                val angle = (animatedScore.value) * 360 / 100
                val size = Size(this.size.minDimension, this.size.minDimension)

                drawArc(
                    color = inactiveColor,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = lineThicknessInactive.toPx(), cap = StrokeCap.Round),
                    size = size,
                    topLeft = Offset(
                        x = this.center.x - (size.width / 2),
                        y = this.center.y - (size.height / 2)
                    )
                )

                drawArc(
                    color = foregroundColor,
                    startAngle = -90f,
                    sweepAngle = angle,
                    useCenter = false,
                    style = Stroke(width = lineThickness.toPx(), cap = StrokeCap.Round),
                    size = size,
                    topLeft = Offset(
                        x = this.center.x - (size.width / 2),
                        y = this.center.y - (size.height / 2)
                    )
                )

            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = stringResource(id = R.string.detail_score),
                    style = Typography.displayMedium,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "${animatedScore.value.toInt()}%",
                    color = MaterialTheme.colorScheme.primary,
                    style = Typography.displayLarge,
                    textAlign = TextAlign.Center
                )

            }

        }

    }

}

@Composable
fun DetailCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
) {

    OutlinedCard(
        modifier = modifier,
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                text = title,
                style = Typography.displaySmall,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                color = MaterialTheme.colorScheme.primary,
                text = value,
                style = Typography.displayMedium,
                textAlign = TextAlign.Center
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
            navigateUp = { },
            navigateToEditHabit = {}
        )
    }
}