package com.willbsp.habits.helper

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText

fun hasContentDescriptionId(
    @StringRes id: Int,
    activity: ComponentActivity
): SemanticsMatcher {
    return hasContentDescription(activity.getString(id))
}

fun hasTextId(
    @StringRes id: Int,
    activity: ComponentActivity
): SemanticsMatcher {
    return hasText(activity.getString(id))
}