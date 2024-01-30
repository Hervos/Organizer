package com.example.simplecalendar

import android.content.Context
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.YearMonth

class CalendarViewModel : ViewModel() {
    private val sharedPreferencesKey = "SimpleCalendarNotes"
    private val data = mutableMapOf<LocalDate, DayData>()

    // Load all data from SharedPreferences
    fun loadNotesFromSharedPreferences(context: Context) {
        val sharedPreferences = context.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
        sharedPreferences.all.forEach { entry ->
            val key = entry.key
            val date = LocalDate.parse(key.substringBefore('_'))
            val value = entry.value
            val dayData = data.getOrPut(date) { DayData(0f, 0f, "") }

            when {
                key.endsWith("_earnings") -> dayData.earnings = (value as? Float) ?: 0f
                key.endsWith("_spendings") -> dayData.spendings = (value as? Float) ?: 0f
                key == date.toString() -> dayData.note = value as? String ?: ""
            }
        }
    }

    fun getDayData(date: LocalDate): DayData {
        return data[date] ?: DayData(0f, 0f, "")
    }

    fun saveDayData(context: Context, date: LocalDate, dayData: DayData) {
        val sharedPreferences = context.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putFloat("${date}_earnings", dayData.earnings)
            putFloat("${date}_spendings", dayData.spendings)
            putString(date.toString(), dayData.note)
            apply()
        }
        data[date] = dayData
    }

    fun calculateMonthlyProfit(yearMonth: YearMonth): Float {
        val monthDays = yearMonth.lengthOfMonth()
        return (1..monthDays).sumOf { day ->
            val dayData = getDayData(LocalDate.of(yearMonth.year, yearMonth.month, day))
            (dayData.earnings - dayData.spendings).toDouble()
        }.toFloat()
    }


    data class DayData(var earnings: Float, var spendings: Float, var note: String)
}
