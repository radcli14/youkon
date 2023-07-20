package com.dcsim.youkon.android.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dcsim.youkon.Measurement


@Composable
fun MeasurementView(measurement: Measurement) {
    var editedName by remember { mutableStateOf(measurement.name) }
    var editedDescription by remember { mutableStateOf(measurement.description) }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        TextField(
            value = editedName,
            onValueChange = { editedName = it },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.subtitle1
        )
        TextField(
            value = editedDescription,
            onValueChange = { editedDescription = it },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.body1
        )

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
            TextField(
                value = measurement.value.toString(),
                onValueChange = { newValue ->
                    val parsedValue = newValue.toDoubleOrNull()
                    if (parsedValue != null) {
                        measurement.value = parsedValue
                    }
                },
                modifier = Modifier.width(96.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            FromDropdown(measurement = measurement) { unit ->
                if (unit != null) {
                    measurement.unit = unit
                }
            }
        }
    }

    // Update the measurement with edited name and description when focus is lost
    DisposableEffect(Unit) {
        onDispose {
            measurement.name = editedName
            measurement.description = editedDescription
        }
    }
}
