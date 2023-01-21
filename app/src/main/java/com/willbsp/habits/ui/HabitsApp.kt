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

// TODO sort the top bar so can be reused not just for home screen e.g navigateToSettings
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsAppTopBar(
    title: String,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean,
    navigateToSettings: () -> Unit = {},
    navigateUp: () -> Unit = {}
) {
    if (canNavigateBack) {
        CenterAlignedTopAppBar(
            title = { Text(text = title) },
            modifier = modifier,
            navigationIcon = {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.topbar_back)
                    )
                }
            },
        )
    } else {
        CenterAlignedTopAppBar(
            title = { Text(text = title, fontWeight = FontWeight.Bold) },
            modifier = modifier,
            navigationIcon = {
                IconButton(onClick = {/* TODO */ }) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = ""
                    )
                }
            },
            actions = {
                IconButton(onClick = navigateToSettings) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = stringResource(R.string.settings)
                    )
                }
            }
        )
    }
}