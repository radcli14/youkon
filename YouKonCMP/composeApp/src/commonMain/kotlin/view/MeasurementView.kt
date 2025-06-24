package view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import model.YkMeasurement
import purchases.PurchasesViewModel
import viewmodel.MeasurementViewModel

/// The editable form of a single measurement, with a name, description, value, and unit. Name and
/// description are editable text fields, value is a numeric field, and unit is a dropdown.
class MeasurementView(
    val measurement: YkMeasurement,
    private val highlightNameAndDescription: Boolean = false,
    private val highlightValueAndUnit: Boolean = false,
    private val purchases: PurchasesViewModel? = null,
    private val onMeasurementUpdated: (YkMeasurement) -> Unit = {}
) {
    @Composable
    fun Body() {
        val isFocused = remember { mutableStateOf(false) }
        val vm = MeasurementViewModel(measurement, onMeasurementUpdated, isFocused)

        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .onboardingModifier(highlightNameAndDescription)
            ) {
                NameField(vm)
                DescriptionField(vm)
            }
            ValueAndUnitStack(vm)
        }
    }

    /// Editable field for the name of the `YkMeasurement`
    @Composable
    private fun NameField(vm: MeasurementViewModel) {
        BasicTextFieldWithHint(
            value = vm.measurementName.value,
            hint = "name",
            onValueChange = { vm.updateName(it) },
            textStyle = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.secondary
            ),
        )
    }

    /// Editable field for the `about` string of the `YkMeasurement
    @Composable
    private fun DescriptionField(vm: MeasurementViewModel) {
        BasicTextFieldWithHint(
            value = vm.measurementDescription.value,
            hint = "description",
            onValueChange = { vm.updateDescription(it) },
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
        )
    }

    /// Numeric field on the left to modify `value`, and dropdown on the right to modify `unit` of the `YkMeasurement`
    @Composable
    private fun ValueAndUnitStack(vm: MeasurementViewModel) {
        Row(
            modifier = Modifier.onboardingModifier(highlightValueAndUnit),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val isExtended = purchases?.isExtended?.value == true
            val availableUnits = if (isExtended) vm.unit.value.allUnits else vm.unit.value.basicUnits
            MeasurementTextField(
                value = vm.value.value,
                modifier = Modifier.weight(1f),
                controlsAreAbove = true,
                updateMeasurement = { vm.updateValue(it) },
                alignedContent = { alignedModifier ->
                    UnitDropdown(
                        unit = vm.unit.value,
                        availableUnits = availableUnits,
                        isNested = true,
                        isExtended = isExtended,
                        includeUnitless = true,
                        modifier = alignedModifier,
                        onClick = { it?.let { vm.updateUnit(it) } }
                    ).Body()
                }
            )
        }
    }
}
