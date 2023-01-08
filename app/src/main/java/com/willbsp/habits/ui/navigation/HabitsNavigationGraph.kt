package com.willbsp.habits.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.willbsp.habits.ui.add.AddHabitScreen
import com.willbsp.habits.ui.home.HomeScreen

@Composable
fun HabitsNavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = "home", // TODO
        modifier = modifier
    ) {
        composable(route = "home") { // TODO make into an enum
            HomeScreen(
                navigateToAddHabit = { navController.navigate("addhabitscreen") }
            )
        }
        composable(route = "addhabitscreen") {
            AddHabitScreen(
                navigateUp = { navController.navigateUp() },
                navigateBack = { navController.popBackStack() }
            )
        }
        // TODO when it comes to adding edit screen can use composable(arguments = x) for habit
    }
}