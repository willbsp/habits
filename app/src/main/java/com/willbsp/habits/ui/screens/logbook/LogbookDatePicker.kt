package com.willbsp.habits.ui.screens.logbook

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.willbsp.habits.ui.theme.Typography
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DatePickerCard(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate,
    onSelectedDateChange: (LocalDate) -> (Unit),
) {

    val startDate by remember {
        mutableStateOf(
            selectedDate.minusWeeks((Integer.MAX_VALUE / 2).toLong()).with(DayOfWeek.MONDAY)
        ) // TODO look into locales
    }

    Card(modifier = modifier) {

        Spacer(Modifier.height(10.dp))

        Text(
            text = selectedDate.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH),
            style = Typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = rememberPagerState(Integer.MAX_VALUE / 2),
            count = Integer.MAX_VALUE
        ) {

            val date = startDate.plusDays(it.toLong() * 7)

            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {

                repeat(7) { day ->

                    DateIconButton(
                        modifier = Modifier
                            .size(50.dp),
                        date = date.plusDays(day.toLong()),
                        checked = date.plusDays(day.toLong()) == selectedDate,
                        onCheckedChange = { date ->
                            //selectedDate = date
                            onSelectedDateChange(date)
                        }
                    )

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
    onCheckedChange: (LocalDate) -> (Unit)
) {

    val weekday = date.dayOfWeek.getDisplayName(
        TextStyle.SHORT,
        Locale.ENGLISH
    ) // TODO get other locales
    val dayOfMonth = date.dayOfMonth.toString()

    FilledIconToggleButton(
        modifier = modifier,
        checked = checked,
        onCheckedChange = {
            onCheckedChange(date)
        }
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(weekday, style = Typography.bodyLarge)
            Text(dayOfMonth, style = Typography.bodyLarge)
        }
    }

}

@Preview
@Composable
fun DatePickerPreview() {
    DatePickerCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp),
        onSelectedDateChange = {},
        selectedDate = LocalDate.now()
    )
}