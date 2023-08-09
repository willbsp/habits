package com.willbsp.habits

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.remember
import com.willbsp.habits.common.REMINDER_NOTIFICATION_CHANNEL_ID
import com.willbsp.habits.ui.HabitsApp
import com.willbsp.habits.ui.theme.HabitsTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainActivity : ComponentActivity() {

    private val IMPORT_SUCCESSFUL = "DATABASE_IMPORT_SUCCESSFUL"
    private val IMPORT_INVALID = "DATABASE_IMPORT_INVALID"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val snackbarHostState = SnackbarHostState()
        if (intent.getBooleanExtra(IMPORT_SUCCESSFUL, false)) {
            intent.removeExtra(IMPORT_SUCCESSFUL)
            CoroutineScope(Dispatchers.Default).launch {
                snackbarHostState.showSnackbar(getString(R.string.home_import_successful))
            }
        } else if (intent.getBooleanExtra(IMPORT_INVALID, false)) {
            intent.removeExtra(IMPORT_INVALID)
            CoroutineScope(Dispatchers.Default).launch {
                snackbarHostState.showSnackbar(getString(R.string.home_import_invalid))
            }
        }

        val name = getString(R.string.reminder_notification_channel)
        val descriptionText = getString(R.string.reminder_notification_channel_desc)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val reminderChannel =
            NotificationChannel(REMINDER_NOTIFICATION_CHANNEL_ID, name, importance)
        reminderChannel.description = descriptionText
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(reminderChannel)

        setContent {
            HabitsTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val windowSize = calculateWindowSizeClass(activity = this)
                    val heightClass = windowSize.heightSizeClass
                    val widthClass = windowSize.widthSizeClass
                    this.requestedOrientation = remember(key1 = windowSize) {
                        if (heightClass == WindowHeightSizeClass.Compact || widthClass == WindowWidthSizeClass.Compact)
                            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        else
                            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                    }
                    HabitsApp(
                        onDatabaseImport = { restartActivityOnImport(it) },
                        snackbarState = snackbarHostState
                    )
                }
            }
        }
    }

    private fun restartActivityOnImport(successful: Boolean) {
        finish()
        if (successful)
            startActivity(intent.putExtra(IMPORT_SUCCESSFUL, true))
        else
            startActivity(intent.putExtra(IMPORT_INVALID, true))
        Runtime.getRuntime().exit(0)
    }

}
