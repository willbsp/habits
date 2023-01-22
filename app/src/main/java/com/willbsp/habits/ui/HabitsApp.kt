package com.willbsp.habits.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.willbsp.habits.R
import com.willbsp.habits.ui.navigation.HabitsNavigationGraph

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HabitsApp(navController: NavHostController = rememberAnimatedNavController()) {
    HabitsNavigationGraph(navController = navController)
}