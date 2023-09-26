package com.willbsp.habits.ui.common.button

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun HabitsFloatingAction(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String
) {
    FloatingActionButton(
        onClick = onClick,
        shape = CircleShape,
        modifier = Modifier
            .navigationBarsPadding()
            .padding(10.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}