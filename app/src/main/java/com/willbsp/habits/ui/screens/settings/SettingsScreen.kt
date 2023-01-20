package com.willbsp.habits.ui.screens.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.willbsp.habits.R
import com.willbsp.habits.ui.HabitsAppTopBar
import com.willbsp.habits.ui.theme.HabitsTheme

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit
) {

    Settings(
        modifier = modifier,
        navigateUp = navigateUp
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Settings(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit
) {

    Scaffold(
        topBar = {
            HabitsAppTopBar(
                title = stringResource(R.string.settings_title),
                canNavigateBack = true,
                navigateUp = navigateUp
            )
        }
    ) { innerPadding ->

        Box(
            modifier = modifier
                .padding(innerPadding)
        ) {

        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SettingsScreenPreview() {
    HabitsTheme {
        Settings {}
    }
}
