package com.willbsp.habits.ui.common.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.willbsp.habits.R

@Composable
fun NotificationPermissionDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    showConfirmButton: Boolean
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Filled.Notifications, contentDescription = null) },
        title = { Text(text = stringResource(R.string.notifications)) },
        text = {
            Text(
                text = stringResource(R.string.notifications_desc),
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            if (showConfirmButton)
                Button(onClick = onConfirm) { Text(stringResource(R.string.edit_confirm)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.edit_dismiss)) }
        }
    )
}

@Preview
@Composable
private fun NotificationPermissionDialogPreview() {
    NotificationPermissionDialog(onConfirm = {}, onDismiss = {}, showConfirmButton = true)
}
