package com.willbsp.habits.ui.screens.detail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.willbsp.habits.R
import com.willbsp.habits.data.model.HabitFrequency
import com.willbsp.habits.ui.common.DefaultHabitsAppTopBar
import com.willbsp.habits.ui.theme.HabitsTheme
import com.willbsp.habits.ui.theme.Typography
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun DetailScreen(
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

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {

                    Icon(imageVector = Icons.Default.CalendarToday, null)
                    if (detailUiState.type == HabitFrequency.DAILY)
                        Text(stringResource(R.string.detail_daily))
                    else if (detailUiState.type == HabitFrequency.WEEKLY)
                        Text(
                            text = pluralStringResource(
                                id = R.plurals.detail_times_per_week,
                                count = detailUiState.repeat,
                                detailUiState.repeat
                            )
                        )

                    Spacer(Modifier.weight(1f))

                    if (detailUiState.reminderDays.isNotEmpty()) {
                        Text(
                            text = if (detailUiState.reminderDays.count() == 7)
                                stringResource(id = R.string.detail_every_day) else
                                pluralStringResource(
                                    id = R.plurals.detail_reminders,
                                    count = detailUiState.reminderDays.count(),
                                    detailUiState.reminderDays.count()
                                )
                        )
                        Icon(imageVector = Icons.Default.NotificationsActive, null)
                    }

                }

            }

            item {

                CircularDetailScoreCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    score = detailUiState.score.toFloat()
                )

            }

            item {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    DetailCard(
                        modifier = modifier
                            .weight(1f),
                        title = stringResource(R.string.detail_streak),
                        value = detailUiState.streak.toString()
                    )

                    DetailCard(
                        modifier = modifier
                            .weight(1f),
                        title = stringResource(R.string.detail_longest_streak),
                        value = detailUiState.longestStreak.toString()
                    )

                }

            }

            item {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    val startedDate =
                        if (detailUiState.started == null) stringResource(R.string.detail_not_started)
                        else {
                            "${detailUiState.started.dayOfMonth} ${
                                detailUiState.started.month.getDisplayName(
                                    TextStyle.SHORT_STANDALONE,
                                    Locale.getDefault()
                                )
                            }"
                        }

                    DetailCard(
                        modifier = modifier.weight(1f),
                        title = stringResource(R.string.detail_started),
                        value = startedDate
                    )

                    DetailCard(
                        modifier = modifier.weight(1f),
                        title = stringResource(R.string.detail_total),
                        value = detailUiState.total.toString()
                    )

                }

            }

            item { Spacer(Modifier.height(10.dp)) }

        }


    }

}


@Composable
fun CircularDetailScoreCard(
    modifier: Modifier = Modifier,
    score: Float,
) {

    var initialScore by rememberSaveable {
        mutableFloatStateOf(0f)
    }

    val animatedScore = animateFloatAsState(
        targetValue = initialScore,
        animationSpec = tween(durationMillis = 1000), label = "animatedScore"
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
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .height(160.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .weight(0.7f),
                contentAlignment = Alignment.Center
            ) {


                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onSurface,
                    text = value,
                    style = Typography.displayMedium,
                    textAlign = TextAlign.Center
                )

            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp))
                    .weight(0.3f),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = title,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = Typography.headlineSmall,
                    textAlign = TextAlign.Center
                )

            }

        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetailScreenPreview() {
    HabitsTheme {
        DetailScreen(
            detailUiState = DetailUiState(
                -1,
                "Flashcards",
                type = HabitFrequency.WEEKLY,
                1,
                5,
                23,
                started = LocalDate.of(2023, 4, 6)
            ),
            navigateUp = { },
            navigateToEditHabit = {}
        )
    }
}