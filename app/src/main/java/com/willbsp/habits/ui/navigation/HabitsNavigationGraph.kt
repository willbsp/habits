package com.willbsp.habits.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.willbsp.habits.ui.screens.add.AddHabitScreen
import com.willbsp.habits.ui.screens.add.AddHabitViewModel
import com.willbsp.habits.ui.screens.detail.DetailScreen
import com.willbsp.habits.ui.screens.detail.DetailViewModel
import com.willbsp.habits.ui.screens.edit.EditHabitScreen
import com.willbsp.habits.ui.screens.edit.EditHabitViewModel
import com.willbsp.habits.ui.screens.home.HomeScreen
import com.willbsp.habits.ui.screens.home.HomeViewModel
import com.willbsp.habits.ui.screens.logbook.LogbookScreen
import com.willbsp.habits.ui.screens.logbook.LogbookViewModel
import com.willbsp.habits.ui.screens.settings.SettingsScreen
import com.willbsp.habits.ui.screens.settings.SettingsViewModel

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
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {

            val viewModel = hiltViewModel<HomeViewModel>()

            HomeScreen(
                viewModel = viewModel,
                navigateToLogbook = {
                    navController.navigate(HabitsNavigationDestination.LOGBOOK.route)
                },
                navigateToAddHabit = {
                    navController.navigate(HabitsNavigationDestination.ADD.route)
                },
                navigateToDetail = { habitId ->
                    navController.navigate(HabitsNavigationDestination.DETAIL.route + habitId)
                },
                navigateToSettings = {
                    navController.navigate(HabitsNavigationDestination.SETTINGS.route)
                }
            )

        }

        composable(
            route = HabitsNavigationDestination.ADD.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            }
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
            route = HabitsNavigationDestination.DETAIL.route + "{habitId}",
            arguments = listOf(navArgument("habitId") { type = NavType.IntType }),
            enterTransition = {
                when (initialState.destination.route) {
                    HabitsNavigationDestination.EDIT.route + "{habitId}" -> fadeIn()
                    else -> slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(300)
                    )
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    HabitsNavigationDestination.EDIT.route + "{habitId}" -> fadeOut()
                    else -> slideOutOfContainer(
                        AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(300)
                    )
                }
            }
        ) {

            val viewModel = hiltViewModel<DetailViewModel>()

            DetailScreen(
                viewModel = viewModel,
                navigateUp = { navController.navigateUp() },
                navigateToEditHabit = { habitId ->
                    navController.navigate(HabitsNavigationDestination.EDIT.route + habitId)
                }
            )

        }

        composable(
            route = HabitsNavigationDestination.EDIT.route + "{habitId}",
            arguments = listOf(navArgument("habitId") { type = NavType.IntType }),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            }

        ) {

            val viewModel = hiltViewModel<EditHabitViewModel>()

            EditHabitScreen(
                viewModel = viewModel,
                navigateUp = {
                    navController.navigateUp()
                },
                navigateBack = {
                    navController.popBackStack()
                },
                navigateToHome = {
                    navController.popBackStack(HabitsNavigationDestination.HOME.route, false)
                }
            )

        }

        composable(
            route = HabitsNavigationDestination.LOGBOOK.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            }
        ) {

            val viewModel = hiltViewModel<LogbookViewModel>()

            LogbookScreen(
                viewModel = viewModel,
                navigateUp = {
                    navController.navigateUp()
                }
            )

        }

        composable(
            route = HabitsNavigationDestination.SETTINGS.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            }
        ) {

            val viewModel = hiltViewModel<SettingsViewModel>()

            SettingsScreen(
                viewModel = viewModel,
                navigateUp = {
                    navController.navigateUp()
                }
            )

        }

    }
}