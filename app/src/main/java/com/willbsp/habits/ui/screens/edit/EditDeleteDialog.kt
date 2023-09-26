package com.willbsp.habits.ui.screens.edit

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
fun EditDeleteDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Filled.Delete, contentDescription = null) },
        title = { Text(text = stringResource(R.string.edit_delete_habit)) },
        text = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.edit_cannot_undo),
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            Button(onClick = onConfirm) { Text(stringResource(R.string.edit_confirm)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.edit_dismiss))
            }
        }
    )
}

@Preview
@Composable
private fun EditDeleteDialogPreview() {
    EditDeleteDialog(onConfirm = {}, onDismiss = {})
}