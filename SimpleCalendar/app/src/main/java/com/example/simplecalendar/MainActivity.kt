package com.example.simplecalendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.simplecalendar.calendar.CalendarScreen
import com.example.simplecalendar.calendar.NoteScreen
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: CalendarViewModel = viewModel()
            viewModel.loadNotesFromSharedPreferences(applicationContext)
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "calendar") {
                composable("calendar") {
                    CalendarScreen(navController, viewModel)
                }
                composable("note/{date}", arguments = listOf(navArgument("date") { type = NavType.StringType })) { backStackEntry ->
                    val dateStr = backStackEntry.arguments?.getString("date")
                    val date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE)
                    NoteScreen(navController, viewModel, date)
                }
            }
        }
    }
}