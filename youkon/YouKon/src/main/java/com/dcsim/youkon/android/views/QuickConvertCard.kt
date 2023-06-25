package com.dcsim.youkon.android.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.dcsim.youkon.testMeasurement

@Composable
fun QuickConvertCard() {
    val measurement by remember { mutableStateOf(testMeasurement()) }
    var text by remember { mutableStateOf(TextFieldValue(measurement.value.toString())) }

    Card(
        modifier = Modifier
            .requiredWidth(360.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // The label at the top of the card
            Text("Quick Convert",
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.h6
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // The field that takes the user input on the numeric value of the measurement
                //MeasurementTextField(measurement = measurement)
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

                // Selection for which type of unit to convert from
                FromDropdown(measurement = measurement)
                ToDropdown(measurement = measurement)
            }

            // The display of the measurement after conversion
            Text(measurement.convertTo("feet").toString())
        }
    }
}
