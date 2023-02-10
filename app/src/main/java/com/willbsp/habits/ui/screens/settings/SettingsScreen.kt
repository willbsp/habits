package com.willbsp.habits.ui.screens.settings

import androidx.annotation.StringRes
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
import com.willbsp.habits.ui.common.PreferencesUiState
import com.willbsp.habits.ui.theme.HabitsTheme
import com.willbsp.habits.ui.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel,
    navigateUp: () -> Unit
) {

    val preferencesUiState by viewModel.preferencesUiState.collectAsState(PreferencesUiState())
    val coroutineScope = rememberCoroutineScope()

    Settings(
        modifier = modifier,
        navigateUp = navigateUp,
        onShowStreaksPressed = {
            coroutineScope.launch { viewModel.saveStreaksPreference(it) }
        },
        onShowSubtitlePressed = {
            coroutineScope.launch { viewModel.saveSubtitlePreference(it) }
        },
        preferencesUiState = preferencesUiState
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Settings(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    onShowStreaksPressed: (Boolean) -> Unit,
    onShowSubtitlePressed: (Boolean) -> Unit,
    preferencesUiState: PreferencesUiState
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

            SettingItem(
                checked = preferencesUiState.showStreaks,
                onCheckedChange = onShowStreaksPressed,
                title = R.string.settings_display_streaks,
                subtitle = R.string.settings_display_streaks_subtitle
            )

            SettingItem(
                checked = preferencesUiState.showCompletedSubtitle,
                onCheckedChange = onShowSubtitlePressed,
                title = R.string.settings_completed_subtitle,
                subtitle = R.string.settings_completed_subtitle_desc
            )

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingItem(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    @StringRes title: Int,
    @StringRes subtitle: Int
) {
    ListItem(
        modifier = modifier.fillMaxWidth(),
        headlineText = {
            Text(
                text = stringResource(title),
                style = Typography.titleLarge
            )
        },
        supportingText = {
            Text(
                text = stringResource(subtitle),
                style = Typography.bodyMedium
            )
        },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SettingsScreenPreview() {
    HabitsTheme {
        //Settings {}
    }
}
