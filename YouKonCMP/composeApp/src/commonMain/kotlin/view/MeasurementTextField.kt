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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import canBeInt
import countSignificantDigits
import numericValueEquals
import toDoubleOrZeroOrNull
import viewmodel.MeasurementTextFieldViewModel


@Composable
fun MeasurementTextField(
    initialText: String,
    modifier: Modifier = Modifier,
    unitText: String? = null,
    controlsAreAbove: Boolean = false,
    updateMeasurement: (Double) -> Unit,
    alignedContent: @Composable (Modifier) -> Unit
) {
    val viewModel = remember { MeasurementTextFieldViewModel(initialText, updateMeasurement) }
    var isFocused by remember { mutableStateOf(false) }
    val textStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.SemiBold
    )

    // Update text when initialText changes
    LaunchedEffect(initialText) {
        viewModel.updateText(initialText)
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
            var trailingIcon: @Composable (() -> Unit)? = null
            unitText?.let {
                trailingIcon = {
                    TextWithSubscripts(
                        it,
                        style = textStyle,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            CustomDecimalTextField(
                viewModel.text,
                textStyle,
                Modifier.weight(1f).onFocusChanged { isFocused = it.hasFocus },
                trailingIcon = trailingIcon
            ) { newText ->
                viewModel.updateText(newText.text, newText.selection.start)
                newText.text.toDoubleOrZeroOrNull()?.let {
                    updateMeasurement(it)
                }
            }

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
        onValueChange = { newText ->
            // Ensure negative sign is at the start
            val text = newText.text
            val hasNegativeSign = text.contains("-")
            val withoutNegative = text.replace("-", "")
            val finalText = if (hasNegativeSign) "-$withoutNegative" else withoutNegative

            // Preserve cursor position
            val oldCursorPos = newText.selection.start
            val newCursorPos = when {
                // If we moved the negative sign to the start, adjust cursor position
                text.contains("-") && text.first() != '-' -> oldCursorPos - 1
                // If we removed a negative sign from the middle, adjust cursor position
                !text.contains("-") && oldCursorPos > 0 && text[oldCursorPos - 1] == '-' -> oldCursorPos - 1
                else -> oldCursorPos
            }.coerceIn(0, finalText.length)

            onValueChange(TextFieldValue(
                text = finalText,
                selection = TextRange(newCursorPos)
            ))
        },
        textStyle = textStyle.copy(textAlign = TextAlign.End),
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.33f),
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.69f)
        )
    )
}
