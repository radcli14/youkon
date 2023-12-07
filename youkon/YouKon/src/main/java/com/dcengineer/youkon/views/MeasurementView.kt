package com.dcengineer.youkon.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dcengineer.youkon.YkMeasurement
import com.dcengineer.youkon.viewmodels.MeasurementViewModel

/// The editable form of a single measurement, with a name, description, value, and unit. Name and
/// description are editable text fields, value is a numeric field, and unit is a dropdown.
class MeasurementView(measurement: YkMeasurement) {
    private val vm = MeasurementViewModel(measurement)

    @Composable
    fun Body() {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            NameField()
            DescriptionField()
            ValueAndUnitStack()
        }
    }

    /// Editable field for the name of the `YkMeasurement`
    @Composable
    private fun NameField() {
        BasicTextFieldWithHint(
            value = vm.measurementName,
            hint = "name",
            onValueChange = { vm.updateName(it) },
            textStyle = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.secondary
            ),
        )
    }

    /// Editable field for the `about` string of the `YkMeasurement
    @Composable
    private fun DescriptionField() {
        BasicTextFieldWithHint(
            value = vm.measurementDescription,
            hint = "description",
            onValueChange = { vm.updateDescription(it) },
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
        )
    }

    /// Numeric field on the left to modify `value`, and dropdown on the right to modify `unit` of the `YkMeasurement`
    @Composable
    private fun ValueAndUnitStack() {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MeasurementTextField(
                initialText = vm.value.toString(),
                modifier = Modifier.weight(1f),
                updateMeasurement = { vm.updateValue(it) }
            )
            UnitDropdown(
                unit = vm.unit,
                availableUnits = vm.unit.allUnits,
                modifier = Modifier.weight(1f),
                onClick = { vm.updateUnit(it) }
            ).Body()
        }
    }
}
