package view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp


@Composable
fun MeasurementEditingControls(
    onPlusMinusClick: () -> Unit,
    onTimesTenClick: () -> Unit,
    onDivideByTenClick: () -> Unit,
    onClearValueClick: () -> Unit
) {
    val localFocusManager = LocalFocusManager.current
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()) {
        MeasurementEditingButton("±", onPlusMinusClick)
        MeasurementEditingButton("×10", onTimesTenClick)
        MeasurementEditingButton("÷10", onDivideByTenClick)
        MeasurementEditingButton("Clear", onClearValueClick)
        IconButton(onClick = localFocusManager::clearFocus) {
            Icon(
                imageVector = Icons.TwoTone.CheckCircle,
                contentDescription = "Confirm",
            )
        }
    }
}

@Composable
fun MeasurementEditingButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.requiredWidthIn(min = 56.dp, max = 96.dp),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.65f),
            contentColor = MaterialTheme.colorScheme.secondary
        ),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        Text(text)
    }
}
