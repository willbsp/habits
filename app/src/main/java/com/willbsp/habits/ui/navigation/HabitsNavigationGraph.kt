package com.willbsp.habits.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.willbsp.habits.ui.add.AddHabitScreen
import com.willbsp.habits.ui.home.HomeScreen

enum class HabitsNavigationDestination(val route: String) {
    HOME(route = "home"),
    ADD(route = "add")
}

@Composable
fun HabitsNavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HabitsNavigationDestination.HOME.route,
        modifier = modifier
    ) {
        composable(route = HabitsNavigationDestination.HOME.route) {
            HomeScreen(
                navigateToAddHabit = {
                    navController.navigate(HabitsNavigationDestination.ADD.route)
                }
            )
        }
        composable(route = HabitsNavigationDestination.ADD.route) {
            AddHabitScreen(
                navigateUp = {
                    navController.navigateUp()
                },
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
        // TODO when it comes to adding edit screen can use composable(arguments = x) for habit
    }
}