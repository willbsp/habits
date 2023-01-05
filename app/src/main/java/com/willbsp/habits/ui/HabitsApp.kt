package com.willbsp.habits.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.willbsp.habits.R
import com.willbsp.habits.ui.navigation.HabitsNavigationGraph

@Composable
fun HabitsApp(navController: NavHostController = rememberNavController()) {
    HabitsNavigationGraph(navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsAppTopBar(
    title: String,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean,
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
            }
        )
    } else {
        CenterAlignedTopAppBar(title = { Text(title) }, modifier = modifier)
    }
}