package com.dcsim.youkon.android.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dcsim.youkon.Measurement
import com.dcsim.youkon.MeasurementUnit
import com.dcsim.youkon.wembyHeight

@Composable
fun QuickConvertCard() {
    val measurement = Measurement(2.26, MeasurementUnit.METERS)
    var equivalentUnits by remember { mutableStateOf(measurement.unit.equivalentUnits()) }
    var targetUnit by remember { mutableStateOf(MeasurementUnit.FEET) }
    var sourceText by remember { mutableStateOf(measurement.toString()) }
    var convertedText by remember { mutableStateOf(measurement.convertTo(targetUnit).toString()) }

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
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // The field that takes the user input on the numeric value of the measurement
                MeasurementTextField(measurement = measurement) {
                    sourceText = measurement.toString()
                    convertedText = measurement.convertTo(targetUnit).toString()
                }

                // Selection for which type of unit to convert from
                FromDropdown(measurement = measurement) { unit ->
                    if (unit != null) {
                        measurement.unit = unit
                        equivalentUnits = measurement.unit.equivalentUnits()
                        if (equivalentUnits.isNotEmpty()) {
                            targetUnit = equivalentUnits.first { it != unit }
                            sourceText = measurement.toString()
                            convertedText = measurement.convertTo(targetUnit).toString()
                        }
                    }
                }
                ToDropdown(equivalentUnits = equivalentUnits, targetUnit = targetUnit)
            }

            // The display of the measurement after conversion
            Text("$sourceText = $convertedText",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(top = 8.dp, start = 14.dp)
            )
        }
    }
}
