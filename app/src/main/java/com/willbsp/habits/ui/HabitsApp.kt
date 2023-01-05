package com.willbsp.habits.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.willbsp.habits.ui.navigation.HabitsNavigationGraph

@Composable
fun HabitsApp(navController: NavHostController = rememberNavController()) {
    HabitsNavigationGraph(navController = navController)
}