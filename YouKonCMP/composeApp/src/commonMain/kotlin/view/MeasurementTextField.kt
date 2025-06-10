package view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun MeasurementTextField(
    initialText: String,
    modifier: Modifier = Modifier,
    unitText: String? = null,
    updateMeasurement: (Double) -> Unit
) {
    var text by remember { mutableStateOf(TextFieldValue(initialText)) }
    val textStyle = MaterialTheme.typography.titleMedium

    // Add this LaunchedEffect to update text when initialText changes, specifically if triggered by the switchSign
    LaunchedEffect(initialText) {
        text = TextFieldValue(initialText)
    }

    Surface(
        modifier = modifier.height(40.dp),
        color = grayBackground.copy(alpha = 0.55f),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            CustomDecimalTextField(text, Modifier.weight(1f)) { newText ->
                if (newText.text.toDoubleOrNull() != null) {
                    text = newText
                    updateMeasurement(newText.text.toDouble())
                } else if (newText.text.isBlank() || newText.text == "-") {
                    text = newText
                    updateMeasurement(0.0)
                }
            }
            unitText?.let {
                TextWithSubscripts(it,
                    style = textStyle,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

/// Intended to hold decimal field with additional buttons over top
@Composable
fun CustomDecimalTextField(
    value: TextFieldValue,
    modifier: Modifier,
    onValueChange: (TextFieldValue) -> Unit,
) {
    val textStyle = MaterialTheme.typography.titleMedium.copy(
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.End
    )

    BasicTextField(
        value = value,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        onValueChange = { newText ->
            onValueChange(newText)
        },
        textStyle = textStyle.copy(
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End
        ),
    )
}
