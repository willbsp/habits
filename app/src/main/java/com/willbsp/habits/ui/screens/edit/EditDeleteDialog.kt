package com.willbsp.habits.ui.screens.edit

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun EditDeleteDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Filled.Delete, contentDescription = null) },
        title = { Text(text = "Delete habit?") },
        text = { Text(text = "This action cannot be undone.") },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Preview
@Composable
fun DetailDeleteDialogPreview() {
    EditDeleteDialog(onConfirm = {}, onDismiss = {})
}