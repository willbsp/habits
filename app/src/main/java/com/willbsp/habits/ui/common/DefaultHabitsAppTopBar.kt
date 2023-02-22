package com.willbsp.habits.ui.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.willbsp.habits.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultHabitsAppTopBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
    navigateUp: () -> Unit = {}
) {

    CenterAlignedTopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.topbar_back)
                    )
                }
            }
        },
        actions = actions
    )

}