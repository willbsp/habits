package com.willbsp.habits.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.willbsp.habits.R
import com.willbsp.habits.ui.common.DefaultHabitsAppTopBar
import com.willbsp.habits.ui.theme.HabitsTheme
import com.willbsp.habits.ui.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel,
    navigateUp: () -> Unit
) {

    val settingsUiState by viewModel.settingsUiState.collectAsState(SettingsUiState())
    val coroutineScope = rememberCoroutineScope()

    Settings(
        modifier = modifier,
        navigateUp = navigateUp,
        onDisplayStreaksPressed = {
            coroutineScope.launch { viewModel.updateSetting(it) }
        },
        settingsUiState = settingsUiState
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Settings(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    onDisplayStreaksPressed: (Boolean) -> Unit,
    settingsUiState: SettingsUiState
) {

    Scaffold(
        topBar = {
            DefaultHabitsAppTopBar(
                title = stringResource(R.string.settings_title),
                canNavigateBack = true,
                navigateUp = navigateUp
            )
        }
    ) { innerPadding ->

        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            ListItem(
                modifier = Modifier.fillMaxWidth(),
                headlineText = {
                    Text(
                        text = "Display Streaks",
                        style = Typography.titleLarge
                    )
                },
                supportingText = {
                    Text(
                        text = "Display streaks on home screen",
                        style = Typography.bodyMedium
                    )
                },
                trailingContent = {
                    Switch(
                        checked = settingsUiState.showStreaksOnHome,
                        onCheckedChange = { onDisplayStreaksPressed(it) })
                }
            )

        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SettingsScreenPreview() {
    HabitsTheme {
        //Settings {}
    }
}
