package viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import canBeInt
import countSignificantDigits
import numericValueEquals
import toDoubleOrZeroOrNull

class MeasurementTextFieldViewModel(initialText: String = ""): ViewModel() {
    var text by mutableStateOf(TextFieldValue(initialText))
    var significantDigits by mutableStateOf(initialText.countSignificantDigits())

    fun updateText(newText: String, cursorPosition: Int? = null) {
        val oldText = text.text
        val oldCursorPos = cursorPosition ?: text.selection.start

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
