package com.willbsp.habits.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.willbsp.habits.ui.common.form.HabitFormUiState
import com.willbsp.habits.ui.screens.about.AboutScreen
import com.willbsp.habits.ui.screens.add.AddScreen
import com.willbsp.habits.ui.screens.add.AddViewModel
import com.willbsp.habits.ui.screens.detail.DetailScreen
import com.willbsp.habits.ui.screens.detail.DetailViewModel
import com.willbsp.habits.ui.screens.edit.EditScreen
import com.willbsp.habits.ui.screens.edit.EditViewModel
import com.willbsp.habits.ui.screens.home.HomeScreen
import com.willbsp.habits.ui.screens.home.HomeViewModel
import com.willbsp.habits.ui.screens.logbook.LogbookScreen
import com.willbsp.habits.ui.screens.logbook.LogbookViewModel
import com.willbsp.habits.ui.screens.settings.SettingsScreen
import com.willbsp.habits.ui.screens.settings.SettingsViewModel

@Composable
fun HabitsNavigationGraph(
    navController: NavHostController,
    onDatabaseImport: (Boolean) -> Unit,
    snackbarState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    NavHost(
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
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            HomeScreen(
                homeUiState = state,
                snackbarHostState = snackbarState,
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
                },
                completedOnClick = { id, date ->
                    viewModel.toggleEntry(id, date)
                }
            )

        }

        composable(
            route = HabitsNavigationDestination.ADD.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            }
        ) {

            val viewModel = hiltViewModel<AddViewModel>()
            val state = viewModel.uiState

            AddScreen(
                navigateUp = {
                    navController.navigateUp()
                },
                onSaveClick = {
                    if (viewModel.saveHabit()) {
                        navController.popBackStack()
                    }
                },
                onValueChange = {
                    viewModel.updateUiState(it)
                },
                formUiState = state
            )

        }

        composable(
            route = HabitsNavigationDestination.DETAIL.route + "{habitId}",
            arguments = listOf(navArgument("habitId") { type = NavType.IntType }),
            enterTransition = {
                when (initialState.destination.route) {
                    HabitsNavigationDestination.EDIT.route + "{habitId}" -> fadeIn()
                    else -> slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300)
                    )
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    HabitsNavigationDestination.EDIT.route + "{habitId}" -> fadeOut()
                    else -> slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(300)
                    )
                }
            }
        ) {

            val viewModel = hiltViewModel<DetailViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            DetailScreen(
                detailUiState = state,
                navigateUp = {
                    navController.navigateUp()
                },
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
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            }

        ) {

            val viewModel = hiltViewModel<EditViewModel>()
            val state = viewModel.uiState

            EditScreen(
                navigateUp = {
                    navController.navigateUp()
                },
                onSaveClick = {
                    if (viewModel.saveHabit())
                        navController.popBackStack()
                },
                onDeleteClick = {
                    viewModel.deleteHabit()
                    navController.popBackStack(HabitsNavigationDestination.HOME.route, false)
                },
                onValueChange = {
                    viewModel.updateUiState(it as HabitFormUiState.Data)
                },
                formUiState = state
            )

        }

        composable(
            route = HabitsNavigationDestination.LOGBOOK.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            }
        ) {

            val viewModel = hiltViewModel<LogbookViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            LogbookScreen(
                logbookUiState = state,
                completedOnClick = { date ->
                    viewModel.toggleEntry(date)
                },
                habitOnClick = { habitId ->
                    viewModel.setSelectedHabit(habitId)
                },
                navigateUp = {
                    navController.navigateUp()
                }
            )

        }

        composable(
            route = HabitsNavigationDestination.SETTINGS.route,
            enterTransition = {
                when (initialState.destination.route) {
                    HabitsNavigationDestination.ABOUT.route -> fadeIn()
                    else -> slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300)
                    )
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    HabitsNavigationDestination.ABOUT.route -> fadeOut()
                    else -> slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(300)
                    )
                }
            }

        ) {

            val context = LocalContext.current
            val viewModel = hiltViewModel<SettingsViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            SettingsScreen(
                navigateUp = {
                    navController.navigateUp()
                },
                navigateToAboutScreen = {
                    navController.navigate(HabitsNavigationDestination.ABOUT.route)
                },
                onShowStatisticPressed = {
                    viewModel.saveStatisticPreference(it)
                },
                onShowSubtitlePressed = {
                    viewModel.saveSubtitlePreference(it)
                },
                onShowScorePressed = {
                    viewModel.saveScorePreference(it)
                },
                onExportPressed = { destination ->
                    if (destination != null) {
                        val output = context.contentResolver.openOutputStream(destination)
                        if (output != null) {
                            viewModel.exportDatabase(output)
                        }
                    }
                },
                onImportPressed = { source ->
                    if (source != null) {
                        val input = context.contentResolver.openInputStream(source)
                        if (input != null) {
                            viewModel.importDatabase(input, onDatabaseImport)
                        }
                    }
                },
                settingsUiState = state
            )

        }

        composable(
            route = HabitsNavigationDestination.ABOUT.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            }
        ) {

            AboutScreen(
                navigateUp = {
                    navController.navigateUp()
                }
            )

        }

    }
}