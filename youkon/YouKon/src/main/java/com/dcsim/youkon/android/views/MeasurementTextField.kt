package com.dcsim.youkon.android.views

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun MeasurementTextField(initialText: String, updateMeasurement: (Double) -> Unit) {
    var text by remember { mutableStateOf(TextFieldValue(initialText)) }
    Surface(
        modifier = Modifier.height(40.dp),
        color = grayBackground,
        shape = RoundedCornerShape(8.dp)
    ) {
        BasicTextField(
            value = text,
            modifier = Modifier.padding(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            onValueChange = { newText ->
                if (newText.text.toDoubleOrNull() != null) {
                    text = newText
                    updateMeasurement(newText.text.toDouble())
                } else if (newText.text.isBlank() || newText.text == "-") {
                    text = newText
                    updateMeasurement(0.0)
                }
            },
            textStyle = MaterialTheme.typography.h6.copy(
                color = MaterialTheme.colors.onSurface,
                textAlign = TextAlign.End
            ),
        )
    }

}