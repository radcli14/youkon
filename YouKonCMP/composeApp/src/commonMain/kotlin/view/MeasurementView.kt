package view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.unit.dp
import defaultPadding
import model.YkMeasurement
import org.jetbrains.compose.resources.stringResource
import purchases.PurchasesViewModel
import viewmodel.MeasurementViewModel
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.description_hint
import youkon.composeapp.generated.resources.name_hint
import youkon.composeapp.generated.resources.value

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

        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.defaultPadding()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onboardingModifier(highlightNameAndDescription)
                ) {
                    NameField(vm)
                    DescriptionField(vm)
                }
                ValueAndUnitStack(vm)
            }
        }


    }

    /// Editable field for the name of the `YkMeasurement`
    @Composable
    private fun NameField(vm: MeasurementViewModel) {
        OutlinedTextField(
            value = vm.measurementName.value,
            onValueChange = vm::updateName,
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.titleMedium,
            label = { Text(stringResource(Res.string.name_hint)) },
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = MaterialTheme.colorScheme.secondary
            ),
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )
    }

    /// Editable field for the `about` string of the `YkMeasurement
    @Composable
    private fun DescriptionField(vm: MeasurementViewModel) {
        OutlinedTextField(
            value = vm.measurementDescription.value,
            onValueChange = vm::updateDescription,
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium,
            label = { Text(stringResource(Res.string.description_hint)) },
            colors = TextFieldDefaults.colors(),
            shape = MaterialTheme.shapes.medium
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
                label = stringResource(Res.string.value),
                modifier = Modifier.weight(1f),
                borderColor = MaterialTheme.colorScheme.secondary,
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
                        onClick = { it?.let { vm.updateUnit(it) } },
                        onShowPaywall = { purchases?.showPaywall() }
                    ).Body()
                }
            )
        }
    }
}
