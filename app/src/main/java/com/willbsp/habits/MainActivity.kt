package com.willbsp.habits

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.remember
import com.willbsp.habits.ui.HabitsApp
import com.willbsp.habits.ui.theme.HabitsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                    HabitsApp()
                }
            }
        }
    }

}
