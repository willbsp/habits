package com.willbsp.habits.ui.screens.logbook

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.willbsp.habits.ui.theme.Typography
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LogbookDatePicker(
    modifier: Modifier = Modifier,
    dates: List<LocalDate>,
    dateOnClick: (LocalDate) -> Unit
) {

    val pagerState = rememberPagerState(Integer.MAX_VALUE)
    VerticalPager(
        modifier = modifier,
        state = pagerState,
        horizontalAlignment = Alignment.CenterHorizontally,
        pageCount = Integer.MAX_VALUE,
        key = { it }
    ) {
        val date = LocalDate.now().minusMonths(Integer.MAX_VALUE - it.toLong() - 1)
        LogbookMonth(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    // Calculate the absolute offset for the current page from the
                    // scroll position. We use the absolute value which allows us to mirror
                    // any effects for both directions
                    val pageOffset =
                        ((pagerState.currentPage - it) + pagerState.currentPageOffsetFraction).absoluteValue

                    scaleX = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )

                    scaleY = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )

                    // We animate the alpha, between 50% and 100%
                    alpha = lerp(
                        start = 0f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                },
            date = date,
            checkedDates = dates,
            dateOnClick = dateOnClick
        )
    }

}

// TODO could these be animated to fade in when the user scrolls to them
// TODO need to show weekdays
@Composable
fun LogbookMonth(
    modifier: Modifier = Modifier,
    date: LocalDate,
    dateOnClick: (LocalDate) -> Unit,
    checkedDates: List<LocalDate>
) {

    val startDate = date.withDayOfMonth(1).with(DayOfWeek.MONDAY)
    val today = LocalDate.now()

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            date.month.getDisplayName(TextStyle.FULL, Locale.getDefault()),
            style = Typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            repeat(7) { row ->
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    repeat(5) { col ->
                        val currentDate = startDate.plusWeeks(col.toLong()).plusDays(row.toLong())
                        if (currentDate.month == date.month) {
                            DateIconButton(
                                modifier = Modifier.size(40.dp),
                                date = currentDate,
                                checked = checkedDates.contains(currentDate),
                                enabled = !currentDate.isAfter(today),
                                onCheckedChange = { dateOnClick(currentDate) }
                            )
                        } else {
                            Box(Modifier.size(40.dp))
                        }
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun DateIconButton(
    modifier: Modifier = Modifier,
    date: LocalDate,
    checked: Boolean,
    enabled: Boolean,
    onCheckedChange: (LocalDate) -> (Unit)
) {
    val dayOfMonth = date.dayOfMonth.toString()

    AnimatedContent(targetState = checked) {
        FilledIconToggleButton(
            modifier = modifier,
            checked = checked,
            enabled = enabled,
            onCheckedChange = {
                onCheckedChange(date)
            }
        ) {
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center
            ) {
                Text(dayOfMonth, style = Typography.bodyLarge)
            }
        }
    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun NewLogbookDatePickerPreview() {
    LogbookDatePicker(
        modifier = Modifier.fillMaxSize(),
        dates = listOf(),
        dateOnClick = { }
    )
}