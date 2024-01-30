package com.example.simplecalendar.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.simplecalendar.CalendarViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun NoteScreen(navController: NavHostController, viewModel: CalendarViewModel, date: LocalDate) {
    val context = LocalContext.current
    val dateString = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    val dayData = viewModel.getDayData(date)

    var earnings by remember { mutableStateOf(dayData.earnings.toString()) }
    var spendings by remember { mutableStateOf(dayData.spendings.toString()) }
    var note by remember { mutableStateOf(dayData.note) }

    var isEditingEarnings by remember { mutableStateOf(false) }
    var isEditingSpendings by remember { mutableStateOf(false) }
    var isEditingNote by remember { mutableStateOf(note.isBlank()) }

    val profit = calculateProfit(earnings, spendings)

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = "Note for day $dateString",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp)
        )

        if (isEditingNote) {
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                label = { Text("Your Note") }
            )
            Button(
                onClick = {
                    viewModel.saveDayData(context, date, CalendarViewModel.DayData(earnings.toFloatOrNull() ?: 0f, spendings.toFloatOrNull() ?: 0f, note))
                    isEditingNote = false
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("Accept Note")
            }
        } else if (note.isNotEmpty()) {
            Text(
                text = note,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
            Button(
                onClick = { isEditingNote = true },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("Edit Note")
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            if (isEditingEarnings) {
                OutlinedTextField(
                    value = earnings,
                    onValueChange = { earnings = it },
                    label = { Text("Earnings") },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        viewModel.saveDayData(context, date, CalendarViewModel.DayData(earnings.toFloatOrNull() ?: 0f, spendings.toFloatOrNull() ?: 0f, note))
                        isEditingEarnings = false
                    },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Text("Accept Earnings")
                }
            } else {
                Text("Earnings: $$earnings")
                Button(
                    onClick = { isEditingEarnings = true },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Text("Edit Earnings")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isEditingSpendings) {
                OutlinedTextField(
                    value = spendings,
                    onValueChange = { spendings = it },
                    label = { Text("Spendings") },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        viewModel.saveDayData(context, date, CalendarViewModel.DayData(earnings.toFloatOrNull() ?: 0f, spendings.toFloatOrNull() ?: 0f, note))
                        isEditingSpendings = false
                    },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Text("Accept Spendings")
                }
            } else {
                Text("Spendings: $$spendings")
                Button(
                    onClick = { isEditingSpendings = true },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Text("Edit Spendings")
                }
            }

            // Display Profit
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "$dateString Profit: $profit",
                style = TextStyle(fontSize = 18.sp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text("Back")
        }
    }
}

private fun calculateProfit(earnings: String, spendings: String): Float {
    return (earnings.toFloatOrNull() ?: 0f) - (spendings.toFloatOrNull() ?: 0f)
}
