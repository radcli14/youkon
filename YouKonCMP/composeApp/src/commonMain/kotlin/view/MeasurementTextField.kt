package view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.layout

@Composable
fun MeasurementTextField(
    initialText: String,
    modifier: Modifier = Modifier,
    unitText: String? = null,
    controlsAreAbove: Boolean = false,
    updateMeasurement: (Double) -> Unit,
    alignedContent: @Composable (Modifier) -> Unit
) {
    var text by remember { mutableStateOf(TextFieldValue(initialText)) }
    var significantDigits by remember { mutableStateOf(initialText.countSignificantDigits()) }
    var isFocused by remember { mutableStateOf(false) }
    val textStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.SemiBold
    )

    // Add this LaunchedEffect to update text while preserving cursor position when initialText changes, specifically if triggered by the switchSign
    LaunchedEffect(initialText) {
        val oldText = text.text
        val newText = initialText
        val oldCursorPos = text.selection.start

        // Calculate new cursor position
        val newCursorPos = when {
            // If we added a minus sign, shift cursor right by 1
            oldText.firstOrNull() != '-' && newText.firstOrNull() == '-' -> oldCursorPos + 1
            // If we removed a negative sign, shift cursor left by 1
            oldText.firstOrNull() == '-' && newText.firstOrNull() != '-' -> (oldCursorPos - 1).coerceAtLeast(0)
            // Otherwise keep cursor at same position
            else -> oldCursorPos
        }

        // Get an updated string, which adapts to whether or not the new string changes the numeric content of the original
        val updatedText: String = when {
            // Check whether the existing text would yield the same number, if so, don't change it.
            newText.numericValueEquals(oldText) -> oldText
            // Check whether the existing text included a decimal point. If it did not, and the update doesn't require a decimal point, convert it to an integer value.
            !oldText.contains(".") && newText.canBeInt -> newText.substringBefore(".")
            // For any other change, format with the current number of significant digits
            else -> {
                val newValue = newText.toDoubleOrZeroOrNull()
                if (newValue != null) {
                    // If the result is an integer, show it without decimal places
                    if (newValue.rem(1.0) == 0.0) {
                        newValue.toInt().toString()
                    } else {
                        newValue.formatWithSignificantDigits(significantDigits)
                    }
                } else {
                    newText
                }
            }
        }

        // Update significant digits if this is a programmatic update (like x10)
        if (newText != oldText) {
            significantDigits = updatedText.countSignificantDigits()
        }

        text = TextFieldValue(
            text = updatedText,
            selection = TextRange(newCursorPos)
        )
    }

    Column(modifier = modifier) {
        AnimatedVisibility(controlsAreAbove && isFocused) {
            MeasurementEditingControls(
                onPlusMinusClick = {
                    val currentValue = text.text.toDoubleOrZeroOrNull() ?: 0.0
                    updateMeasurement(-currentValue)
                },
                onTimesTenClick = {
                    val currentValue = text.text.toDoubleOrZeroOrNull() ?: 0.0
                    updateMeasurement(currentValue * 10)
                },
                onDivideByTenClick = {
                    val currentValue = text.text.toDoubleOrZeroOrNull() ?: 0.0
                    updateMeasurement(currentValue / 10)
                },
                onClearValueClick = {
                    updateMeasurement(0.0)
                }
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Surface(
                color = grayBackground.copy(alpha = 0.55f),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.weight(1f).requiredHeightIn(40.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.alignByBaseline().padding(horizontal = 8.dp)
                ) {
                    CustomDecimalTextField(
                        text,
                        textStyle,
                        Modifier.alignByBaseline().weight(1f).onFocusChanged { isFocused = it.hasFocus }
                    ) { newText ->
                        text = newText
                        // Update significant digits on user input
                        significantDigits = newText.text.countSignificantDigits()
                        newText.text.toDoubleOrZeroOrNull()?.let {
                            updateMeasurement(it)
                        }
                    }
                    unitText?.let {
                        TextWithSubscripts(it,
                            modifier = Modifier.alignByBaseline(),
                            style = textStyle,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            alignedContent(Modifier.alignByBaseline().weight(1f))
        }

        AnimatedVisibility(!controlsAreAbove && isFocused) {
            MeasurementEditingControls(
                onPlusMinusClick = {
                    val currentValue = text.text.toDoubleOrZeroOrNull() ?: 0.0
                    updateMeasurement(-currentValue)
                },
                onTimesTenClick = {
                    val currentValue = text.text.toDoubleOrZeroOrNull() ?: 0.0
                    updateMeasurement(currentValue * 10)
                },
                onDivideByTenClick = {
                    val currentValue = text.text.toDoubleOrZeroOrNull() ?: 0.0
                    updateMeasurement(currentValue / 10)
                },
                onClearValueClick = {
                    updateMeasurement(0.0)
                }
            )
        }
    }
}

fun String.toDoubleOrZeroOrNull(): Double? {
    // First check if this can be converted to a double
    toDoubleOrNull()?.let { return it }
    // Otherwise see if this is blank, or a negative sign, which we assume is equivalent to zero
    if (isBlank() || this == "-") { return 0.0 }
    // Fall through, return null
    return null
}

fun String.numericValueEquals(other: String): Boolean {
    return this.toDoubleOrZeroOrNull() == other.toDoubleOrZeroOrNull()
}

val String.canBeInt: Boolean get() {
    return toDoubleOrZeroOrNull()?.rem(1.0) == 0.0
}

fun String.countSignificantDigits(): Int {
    // Remove any negative sign
    val withoutSign = replace("-", "")
    // Split by decimal point
    val parts = withoutSign.split(".")
    if (parts.size == 1) {
        // No decimal point, count all digits
        return parts[0].length
    }
    // Has decimal point, count all digits
    return parts[0].length + parts[1].length
}

fun Double.formatWithSignificantDigits(digits: Int): String {
    // Convert to string with maximum precision
    val fullString = toString()
    // Split by decimal point
    val parts = fullString.split(".")
    if (parts.size == 1) {
        // No decimal point, just return the integer part
        return parts[0]
    }
    // Has decimal point, format with specified digits but remove trailing zeros
    val formatted = buildString {
        append(parts[0])
        append(".")
        val decimalPart = parts[1]
        val digitsToShow = minOf(digits - parts[0].length, decimalPart.length)
        append(decimalPart.substring(0, digitsToShow))
    }
    return formatted.trimEnd('0').trimEnd('.')
}

/// Intended to hold decimal field with additional buttons over top
@Composable
fun CustomDecimalTextField(
    value: TextFieldValue,
    textStyle: TextStyle,
    modifier: Modifier,
    onValueChange: (TextFieldValue) -> Unit,
) {
    BasicTextField(
        value = value,
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
    )
}
