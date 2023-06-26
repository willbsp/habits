package com.willbsp.habits.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.willbsp.habits.ui.theme.Typography

@Composable
fun FullscreenHint(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    @StringRes iconContentDescription: Int,
    @StringRes text: Int
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                modifier = Modifier.size(100.dp),
                imageVector = icon,
                contentDescription = stringResource(iconContentDescription)
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(text),
                style = Typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}
