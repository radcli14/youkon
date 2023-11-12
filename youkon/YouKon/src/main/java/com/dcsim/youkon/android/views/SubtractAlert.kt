package com.dcsim.youkon.android.views

import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

/// Dialog that is shown when the user taps the `X` button to the left of a project or measurement
@Composable
fun SubtractAlert(
    title: String,
    confirmAction: () -> Unit,
    cancelAction: () -> Unit
) {
    AlertDialog(
        title = { Text("Delete $title") },
        text = { Text("Are you sure?") },
        onDismissRequest = { cancelAction() },
        confirmButton = {
            TextButton(onClick = {
                confirmAction()
            }) {
                Text("Delete",
                    color = MaterialTheme.colors.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = {
                cancelAction()
            }) {
                Text("Cancel")
            }
        }
    )
}
