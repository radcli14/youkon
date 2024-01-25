package view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/// Dialog that is shown when the user taps the `X` button to the left of a project or measurement
@Composable
fun SubtractAlert(
    title: String,
    confirmAction: () -> Unit,
    cancelAction: () -> Unit
) {
    AlertDialog(
        icon = { Icon(Icons.Default.Delete, contentDescription = "Delete Icon") },
        title = { Text("Delete $title") },
        text = { Text("Are you sure?") },
        onDismissRequest = { cancelAction() },
        confirmButton = {
            TextButton(onClick = {
                confirmAction()
            }) {
                Text("Delete",
                    color = MaterialTheme.colorScheme.error
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
