package com.willbsp.habits.ui.screens.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.willbsp.habits.R
import com.willbsp.habits.common.DATABASE_EXPORT_FILE_NAME
import com.willbsp.habits.common.DATABASE_IMPORT_MIME_TYPES
import com.willbsp.habits.ui.common.DefaultHabitsAppTopBar
import com.willbsp.habits.ui.theme.HabitsTheme
import com.willbsp.habits.ui.theme.Typography

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    navigateToAboutScreen: () -> Unit,
    onShowStatisticPressed: (Boolean) -> Unit,
    onShowSubtitlePressed: (Boolean) -> Unit,
    onShowScorePressed: (Boolean) -> Unit,
    onExportPressed: (Uri?) -> Unit,
    onImportPressed: (Uri?) -> Unit,
    settingsUiState: SettingsUiState
) {

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("*/*"),
        onResult = onExportPressed
    )

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = onImportPressed
    )

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

        val importDialogOpen = remember { mutableStateOf(false) }

        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            SettingsHeading(text = stringResource(id = R.string.settings_home))

            SettingToggle(
                checked = settingsUiState.showStatistic,
                onCheckedChange = onShowStatisticPressed,
                title = R.string.settings_display_stats,
                subtitle = R.string.settings_display_stats_desc
            )

            AnimatedVisibility(visible = settingsUiState.showStatistic) {

                SettingToggle(
                    checked = settingsUiState.showScore,
                    onCheckedChange = onShowScorePressed,
                    title = R.string.settings_scores_on_home,
                    subtitle = R.string.settings_scores_on_home_desc
                )

            }

            SettingToggle(
                checked = settingsUiState.showCompletedSubtitle,
                onCheckedChange = onShowSubtitlePressed,
                title = R.string.settings_completed_subtitle,
                subtitle = R.string.settings_completed_subtitle_desc
            )

            SettingsHeading(text = stringResource(id = R.string.settings_backup))

            SettingItem(
                onClick = { exportLauncher.launch(DATABASE_EXPORT_FILE_NAME) },
                title = R.string.settings_export_database,
                subtitle = R.string.settings_export_database_desc
            )

            SettingItem(
                onClick = { importDialogOpen.value = true },
                title = R.string.settings_import_database,
                subtitle = R.string.settings_import_database_desc
            )

        }

        if (importDialogOpen.value) {
            SettingsImportDialog(
                onDismiss = { importDialogOpen.value = false },
                onConfirm = {
                    importLauncher.launch(DATABASE_IMPORT_MIME_TYPES)
                    importDialogOpen.value = false
                }
            )
        }

    }
}

@Composable
private fun SettingsHeading(
    modifier: Modifier = Modifier,
    text: String
) {
    ListItem(
        headlineContent = {
            Text(
                text = text,
                style = Typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    )
}

@Composable
private fun SettingItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @StringRes title: Int,
    @StringRes subtitle: Int
) {
    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
    )
}

@Composable
private fun SettingToggle(
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

@Composable
fun SettingsImportDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Filled.ImportExport, contentDescription = null) },
        title = { Text(text = stringResource(R.string.settings_import_data)) },
        text = { Text(text = stringResource(R.string.settings_import_dialog_desc)) },
        confirmButton = {
            Button(
                onClick = onConfirm
            ) {
                Text(stringResource(R.string.settings_import))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(stringResource(R.string.settings_dismiss))
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SettingsScreenPreview() {
    HabitsTheme {
        SettingsScreen(
            navigateUp = {},
            navigateToAboutScreen = {},
            onShowStatisticPressed = {},
            onShowSubtitlePressed = {},
            onShowScorePressed = {},
            onExportPressed = {},
            onImportPressed = {},
            settingsUiState = SettingsUiState(
                showStatistic = true,
                showScore = true,
                showCompletedSubtitle = false
            )
        )
    }
}
