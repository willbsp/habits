package com.willbsp.habits.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.willbsp.habits.ui.navigation.HabitsNavigationGraph

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HabitsApp(
    navController: NavHostController = rememberAnimatedNavController(),
    onDatabaseImport: () -> Unit,
    snackbarState: SnackbarHostState
) {
    HabitsNavigationGraph(navController, onDatabaseImport, snackbarState)
}