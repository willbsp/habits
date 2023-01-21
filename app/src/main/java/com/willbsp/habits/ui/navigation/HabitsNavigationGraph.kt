package com.willbsp.habits.ui.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.willbsp.habits.ui.screens.add.AddHabitScreen
import com.willbsp.habits.ui.screens.add.AddHabitViewModel
import com.willbsp.habits.ui.screens.home.HomeScreen
import com.willbsp.habits.ui.screens.home.HomeScreenViewModel
import com.willbsp.habits.ui.screens.settings.SettingsScreen

enum class HabitsNavigationDestination(val route: String) {
    HOME(route = "home"),
    ADD(route = "add"),
    EDIT(route = "edit"),
    SETTINGS(route = "settings")
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HabitsNavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = HabitsNavigationDestination.HOME.route,
        modifier = modifier
    ) {

        composable(
            route = HabitsNavigationDestination.HOME.route,
            enterTransition = { slideIntoContainer(AnimatedContentScope.SlideDirection.Right) },
            exitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Left) }
        ) {

            val viewModel = hiltViewModel<HomeScreenViewModel>()

            HomeScreen(
                viewModel = viewModel,
                navigateToAddHabit = {
                    navController.navigate(HabitsNavigationDestination.ADD.route)
                },
                navigateToEditHabit = { habitId ->
                    navController.navigate(HabitsNavigationDestination.EDIT.route)
                },
                navigateToSettings = {
                    navController.navigate(HabitsNavigationDestination.SETTINGS.route)
                }
            )

        }

        composable(
            route = HabitsNavigationDestination.ADD.route,
            enterTransition = { slideIntoContainer(AnimatedContentScope.SlideDirection.Left) },
            exitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Right) }
        ) {

            val viewModel = hiltViewModel<AddHabitViewModel>()

            AddHabitScreen(
                viewModel = viewModel,
                navigateUp = {
                    navController.navigateUp()
                },
                navigateBack = {
                    navController.popBackStack()
                }
            )

        }

        composable(
            route = HabitsNavigationDestination.SETTINGS.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Left)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Right)
            }
        ) {

            SettingsScreen(
                navigateUp = {
                    navController.navigateUp()
                }
            )

        }

        // TODO when it comes to adding edit screen can use composable(arguments = x) for habit

    }
}