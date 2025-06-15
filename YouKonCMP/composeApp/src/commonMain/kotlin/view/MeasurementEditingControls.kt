package view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.CheckCircle
import androidx.compose.material.icons.twotone.Clear
import androidx.compose.material.icons.twotone.ClearAll
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.DeleteSweep
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
    Row(
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        MeasurementEditingButton("±", null, "Change sign", onPlusMinusClick)
        MeasurementEditingButton("×10", null, "Multiply by ten", onTimesTenClick)
        MeasurementEditingButton("÷10", null, "Divide by ten", onDivideByTenClick)
        MeasurementEditingButton(null, Icons.TwoTone.DeleteSweep, "Clear value", onClearValueClick)
        IconButton(onClick = localFocusManager::clearFocus) {
            Icon(
                imageVector = Icons.TwoTone.CheckCircle,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "Confirm",
            )
        }
    }
}

@Composable
fun MeasurementEditingButton(
    text: String? = null,
    icon: ImageVector? = null,
    contentDescription: String = "",
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.65f),
            contentColor = MaterialTheme.colorScheme.secondary
        ),
    ) {
        text?.let { Text(it) }
        icon?.let { Icon(it, contentDescription) }
    }
}
