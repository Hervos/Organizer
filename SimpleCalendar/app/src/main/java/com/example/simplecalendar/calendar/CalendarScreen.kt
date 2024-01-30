package com.example.simplecalendar.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.simplecalendar.CalendarViewModel
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@Composable
fun CalendarScreen(navController: NavHostController, viewModel: CalendarViewModel) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )

        WeekDaysRow()
        MonthView(yearMonth = currentMonth, navController = navController)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                Text("Previous")
            }

            Button(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                Text("Next")
            }
        }

        // Display the current month's profit
        val currentMonthProfit = viewModel.calculateMonthlyProfit(currentMonth)
        Text(
            text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year} Profit: $currentMonthProfit",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )

    }
}

@Composable
fun WeekDaysRow() {
    Row(modifier = Modifier.fillMaxWidth()) {
        val daysOfWeek = listOf(
            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY
        )

        daysOfWeek.forEach { day ->
            Text(
                text = day.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MonthView(yearMonth: YearMonth, navController: NavHostController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(calculateOffset(yearMonth)) {
            Box(modifier = Modifier
                .aspectRatio(1f)
                .padding(4.dp))
        }

        items(yearMonth.lengthOfMonth()) { day ->
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(4.dp)
                    .clickable {
                        navController.navigate("note/${yearMonth.atDay(day + 1)}")
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "${day + 1}")
            }
        }
    }
}

private fun calculateOffset(yearMonth: YearMonth): Int {
    val firstDayOfWeekValue = DayOfWeek.MONDAY.value
    val firstOfMonth = yearMonth.atDay(1).dayOfWeek.value
    return (firstOfMonth - firstDayOfWeekValue + 7) % 7
}
