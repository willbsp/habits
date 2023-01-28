package com.willbsp.habits.ui.screens.logbook

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.willbsp.habits.ui.theme.Typography
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DatePickerCard(
    modifier: Modifier = Modifier,
    today: LocalDate = LocalDate.now(),
    selectedDate: LocalDate,
    onSelectedDateChange: (LocalDate) -> (Unit)
) {

    Card(modifier = modifier) {
        HorizontalPager(modifier = Modifier.fillMaxSize(), count = Integer.MAX_VALUE) {

            //val startDate = today.minusYears(10)
            val startDate = today
            val date = startDate.plusDays(it.toLong() * 7)

            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                repeat(7) { weekday ->
                    var checked = false
                    if (date.plusDays(weekday.toLong()) == selectedDate)
                        checked = true
                    DateIconButton(
                        modifier = Modifier
                            .size(50.dp),
                        date = date.plusDays(weekday.toLong()),
                        checked = checked,
                        onCheckedChange = { date ->
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

    IconToggleButton(
        modifier = modifier,
        checked = checked,
        onCheckedChange = {
            onCheckedChange(date)
        },
        colors = IconButtonDefaults.filledIconToggleButtonColors()
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
            .height(70.dp),
        onSelectedDateChange = {},
        selectedDate = LocalDate.now()
    )
}