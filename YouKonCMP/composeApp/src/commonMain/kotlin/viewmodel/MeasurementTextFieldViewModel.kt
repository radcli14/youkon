package viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import countSignificantDigits
import numericValueEquals
import toDoubleOrZeroOrNull

class MeasurementTextFieldViewModel(
    initialValue: Double = 0.0,
    private val updateMeasurement: (Double) -> Unit
) : ViewModel() {
    private var _text = mutableStateOf(TextFieldValue(initialValue.toString()))
    val text: TextFieldValue
        get() = _text.value

    var significantDigits by mutableStateOf(initialValue.toString().countSignificantDigits())
        private set

    fun updateText(newValue: Double, cursorPosition: Int = newValue.toString().length) {
        val oldText = text.text
        val newText = newValue.toString()
        val oldCursorPos = cursorPosition.coerceIn(
            if (newText.startsWith("-")) 1 else 0,
            newText.length
        )
        val oldSignificantDigits = significantDigits

        // Get an updated string, which adapts to whether or not the new string changes the numeric content of the original
        val updatedText: String = when {
            // Check whether the existing text would yield the same number, if so, don't change it
            newText.numericValueEquals(oldText) -> oldText
            // Check whether the existing text included a decimal point. If it did not, and the update doesn't require a decimal point, convert it to an integer value
            !oldText.contains(".") && newValue.rem(1.0) == 0.0 -> newText.substringBefore(".")
            // For any other change, format with the current number of significant digits
            else -> {
                // If the result is an integer, show it without decimal places
                if (newValue.rem(1.0) == 0.0) {
                    newValue.toInt().toString()
                } else {
                    newValue.formatWithSignificantDigits(significantDigits)
                }
            }
        }

        // Calculate new significant digits
        val newSignificantDigits = updatedText.countSignificantDigits()

        // Only update if the numeric value has changed
        if (!oldText.numericValueEquals(updatedText) || oldSignificantDigits != newSignificantDigits) {
            significantDigits = newSignificantDigits
            _text.value = TextFieldValue(
                text = updatedText,
                selection = TextRange(oldCursorPos)
            )
        }
    }

    fun handleTextChange(newValue: TextFieldValue) {
        // Ensure negative sign is at the start
        val text = newValue.text
        val hasNegativeSign = text.contains("-")
        val withoutNegative = text.replace("-", "")
        val finalText = if (hasNegativeSign) "-$withoutNegative" else withoutNegative

        // Preserve cursor position
        val oldCursorPos = newValue.selection.start
        val newCursorPos = when {
            // If we moved the negative sign to the start, adjust cursor position
            text.contains("-") && text.first() != '-' -> oldCursorPos - 1
            // If we removed a negative sign from the middle, adjust cursor position
            !text.contains("-") && oldCursorPos > 0 && text[oldCursorPos - 1] == '-' -> oldCursorPos - 1
            else -> oldCursorPos
        }.coerceIn(if (finalText.startsWith("-")) 1 else 0, finalText.length)

        // Update text and preserve cursor position
        _text.value = TextFieldValue(
            text = finalText,
            selection = TextRange(newCursorPos)
        )

        // Update measurement if we have a valid number
        finalText.toDoubleOrZeroOrNull()?.let {
            updateMeasurement(it)
        }
    }

    fun handlePlusMinusClick() {
        val currentValue = text.text.toDoubleOrZeroOrNull() ?: 0.0
        updateMeasurement(-currentValue)
    }

    fun handleTimesTenClick() {
        val currentValue = text.text.toDoubleOrZeroOrNull() ?: 0.0
        updateMeasurement(currentValue * 10)
    }

    fun handleDivideByTenClick() {
        val currentValue = text.text.toDoubleOrZeroOrNull() ?: 0.0
        updateMeasurement(currentValue / 10)
    }

    fun handleClearValueClick() {
        updateMeasurement(0.0)
    }

    private fun formatWithSignificantDigits(value: Double, digits: Int): String {
        // Convert to string with maximum precision
        val fullString = value.toString()
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
}

private fun Double.formatWithSignificantDigits(digits: Int): String {
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
