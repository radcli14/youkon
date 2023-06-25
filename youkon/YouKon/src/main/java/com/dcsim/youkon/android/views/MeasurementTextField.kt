package com.dcsim.youkon.android.views

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.dcsim.youkon.Measurement

@Composable
fun MeasurementTextField(measurement: Measurement) {
    var text by remember { mutableStateOf(TextFieldValue(measurement.value.toString())) }

    TextField(
        value = text,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        onValueChange = { newText ->
            if (newText.text.toDoubleOrNull() != null) {
                text = newText
                measurement.value = newText.text.toDouble()
            } else if (newText.text == "") {
                text = newText
                measurement.value = 0.0
            }
        },
        modifier = Modifier.width(96.dp)
    )
}