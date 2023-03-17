package com.willbsp.habits.ui.screens.logbook

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.willbsp.habits.ui.theme.Typography
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun LogbookDatePicker(
    modifier: Modifier = Modifier,
    dates: List<LocalDate>,
    dateOnClick: (LocalDate) -> Unit
) {

    LazyColumn(
        modifier = modifier,
        state = rememberLazyListState(Integer.MAX_VALUE),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        items(count = Integer.MAX_VALUE, key = { it }) {
            val date = LocalDate.now().minusMonths(Integer.MAX_VALUE - it.toLong() - 1)
            LogbookMonth(
                modifier = Modifier.fillMaxWidth(),
                date = date,
                checkedDates = dates,
                dateOnClick = dateOnClick
            )
            Spacer(modifier = Modifier.height(80.dp))
        }

    }

}

// TODO could these be animated to fade in when the user scrolls to them
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
            date.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH),
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

@Composable
private fun DateIconButton(
    modifier: Modifier = Modifier,
    date: LocalDate,
    checked: Boolean,
    enabled: Boolean,
    onCheckedChange: (LocalDate) -> (Unit)
) {
    val dayOfMonth = date.dayOfMonth.toString()

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

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun NewLogbookDatePickerPreview() {
    LogbookDatePicker(
        modifier = Modifier.fillMaxSize(),
        dates = listOf(),
        dateOnClick = { }
    )
}