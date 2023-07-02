package com.willbsp.habits.ui.screens.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.willbsp.habits.R
import com.willbsp.habits.ui.common.DefaultHabitsAppTopBar
import com.willbsp.habits.ui.theme.HabitsTheme
import com.willbsp.habits.ui.theme.Typography

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    navigateToAboutScreen: () -> Unit,
    onShowStreaksPressed: (Boolean) -> Unit,
    onShowSubtitlePressed: (Boolean) -> Unit,
    settingsUiState: SettingsUiState
) {

    Scaffold(
        topBar = {
            DefaultHabitsAppTopBar(
                title = stringResource(R.string.settings_title),
                canNavigateBack = true,
                navigateUp = navigateUp,
                actions = {
                    IconButton(onClick = navigateToAboutScreen) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = stringResource(R.string.settings_about_screen)
                        )
                    }
                }
            )
        },

        ) { innerPadding ->

        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            SettingItem(
                checked = settingsUiState.showStreaks,
                onCheckedChange = onShowStreaksPressed,
                title = R.string.settings_display_streaks,
                subtitle = R.string.settings_display_streaks_subtitle
            )

            SettingItem(
                checked = settingsUiState.showCompletedSubtitle,
                onCheckedChange = onShowSubtitlePressed,
                title = R.string.settings_completed_subtitle,
                subtitle = R.string.settings_completed_subtitle_desc
            )

        }

    }
}

@Composable
private fun SettingItem(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    @StringRes title: Int,
    @StringRes subtitle: Int
) {
    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) },
        headlineContent = {
            Text(
                text = stringResource(title),
                style = Typography.titleLarge
            )
        },
        supportingContent = {
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
