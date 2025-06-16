package view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import viewmodel.MeasurementTextFieldViewModel


@Composable
fun MeasurementTextField(
    value: Double,
    modifier: Modifier = Modifier,
    unitText: String? = null,
    controlsAreAbove: Boolean = false,
    updateMeasurement: (Double) -> Unit,
    alignedContent: @Composable (Modifier) -> Unit
) {
    val viewModel = remember { MeasurementTextFieldViewModel(value, updateMeasurement, unitText) }
    var isFocused by remember { mutableStateOf(false) }
    val textStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.SemiBold
    )

    // Update text when value changes
    LaunchedEffect(value) {
        viewModel.updateText(value)
    }

    Column(modifier = modifier) {
        AnimatedVisibility(controlsAreAbove && isFocused) {
            MeasurementEditingControls(
                onPlusMinusClick = viewModel::handlePlusMinusClick,
                onTimesTenClick = viewModel::handleTimesTenClick,
                onDivideByTenClick = viewModel::handleDivideByTenClick,
                onClearValueClick = viewModel::handleClearValueClick
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomDecimalTextField(
                viewModel.text,
                textStyle,
                Modifier.weight(1f).onFocusChanged { isFocused = it.hasFocus },
                trailingIcon = { viewModel.TrailingIcon(textStyle) },
                onValueChange = viewModel::handleTextChange
            )

            alignedContent(Modifier.weight(1f))
        }

        AnimatedVisibility(!controlsAreAbove && isFocused) {
            MeasurementEditingControls(
                onPlusMinusClick = viewModel::handlePlusMinusClick,
                onTimesTenClick = viewModel::handleTimesTenClick,
                onDivideByTenClick = viewModel::handleDivideByTenClick,
                onClearValueClick = viewModel::handleClearValueClick
            )
        }
    }
}

/// Intended to hold decimal field with additional buttons over top
@Composable
fun CustomDecimalTextField(
    value: TextFieldValue,
    textStyle: TextStyle,
    modifier: Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (TextFieldValue) -> Unit,
) {
    OutlinedTextField(
        value = value,
        trailingIcon = trailingIcon,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        onValueChange = onValueChange,
        textStyle = textStyle.copy(textAlign = TextAlign.End),
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.33f),
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.69f)
        )
    )
}
