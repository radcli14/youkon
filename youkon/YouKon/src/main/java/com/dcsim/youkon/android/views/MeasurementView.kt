package com.dcsim.youkon.android.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dcsim.youkon.YkMeasurement
import com.dcsim.youkon.android.viewmodels.MeasurementViewModel

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
            textStyle = MaterialTheme.typography.subtitle1.copy(
                color = MaterialTheme.colors.primary
            ),
        )
    }

    /// Editable field for the `about` string of the `YkMeasurement
    @Composable
    private fun DescriptionField() {
        //val measurement = vm.measurement.collectAsState()
        BasicTextFieldWithHint(
            value = vm.measurementDescription,
            hint = "description",
            onValueChange = { vm.updateDescription(it) },
            textStyle = MaterialTheme.typography.body1.copy(
                color = MaterialTheme.colors.onSurface
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
