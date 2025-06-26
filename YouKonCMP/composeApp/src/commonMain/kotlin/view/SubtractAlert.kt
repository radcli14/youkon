package view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.are_you_sure
import youkon.composeapp.generated.resources.cancel
import youkon.composeapp.generated.resources.delete
import youkon.composeapp.generated.resources.delete_named

/// Dialog that is shown when the user taps the `X` button to the left of a project or measurement
@Composable
fun SubtractAlert(
    title: String,
    confirmAction: () -> Unit,
    cancelAction: () -> Unit
) {
    AlertDialog(
        icon = { Icon(Icons.Default.Delete, contentDescription = null) },
        title = { Text(stringResource(Res.string.delete_named, title)) },
        text = { Text(stringResource(Res.string.are_you_sure)) },
        onDismissRequest = { cancelAction() },
        confirmButton = {
            TextButton(onClick = confirmAction) {
                Text(
                    text = stringResource(Res.string.delete),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = {
                cancelAction()
            }) {
                Text(stringResource(Res.string.cancel))
            }
        }
    )
}
