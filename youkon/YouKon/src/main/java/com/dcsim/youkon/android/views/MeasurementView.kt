package com.dcsim.youkon.android.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dcsim.youkon.Measurement


@Composable
fun MeasurementView(measurement: Measurement) {
    var editedName by remember { mutableStateOf(measurement.name) }
    var editedDescription by remember { mutableStateOf(measurement.description) }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        BasicTextField(
            value = editedName,
            onValueChange = { editedName = it },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.subtitle1.copy(
                color = MaterialTheme.colors.primary
            ),
        )
        BasicTextField(
            value = editedDescription,
            onValueChange = { editedDescription = it },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.body1.copy(
                color = MaterialTheme.colors.onSurface
            ),
        )

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
            MeasurementTextField(measurement = measurement) {
                // Update the views
            }

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
