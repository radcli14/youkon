package view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.CheckCircle
import androidx.compose.material.icons.twotone.DeleteSweep
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
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.change_sign
import youkon.composeapp.generated.resources.`clear value`
import youkon.composeapp.generated.resources.confirm
import youkon.composeapp.generated.resources.divide_by_ten
import youkon.composeapp.generated.resources.multiply_by_ten


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
        MeasurementEditingButton(text = "±", contentDescriptionRes = Res.string.change_sign, onClick = onPlusMinusClick)
        MeasurementEditingButton(text = "×10", contentDescriptionRes = Res.string.multiply_by_ten, onClick = onTimesTenClick)
        MeasurementEditingButton(text = "÷10", contentDescriptionRes = Res.string.divide_by_ten, onClick = onDivideByTenClick)
        MeasurementEditingButton(icon = Icons.TwoTone.DeleteSweep, contentDescriptionRes = Res.string.`clear value`, onClick = onClearValueClick)
        IconButton(onClick = localFocusManager::clearFocus) {
            Icon(
                imageVector = Icons.TwoTone.CheckCircle,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = stringResource(Res.string.confirm),
            )
        }
    }
}

@Composable
fun MeasurementEditingButton(
    text: String? = null,
    icon: ImageVector? = null,
    contentDescription: String = "",
    contentDescriptionRes: StringResource? = null,
    onClick: () -> Unit
) {
    // Prefer the string resource if provided over the hard string
    var description = contentDescription
    contentDescriptionRes?.let { resource ->
        description = stringResource(resource)
    }

    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.65f),
            contentColor = MaterialTheme.colorScheme.secondary
        ),
    ) {
        text?.let { Text(it) }
        icon?.let { Icon(it, description) }
    }
}
