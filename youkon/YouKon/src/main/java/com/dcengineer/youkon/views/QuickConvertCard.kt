package com.dcengineer.youkon.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dcengineer.youkon.viewmodels.QuickConvertCardViewModel
import com.dcengineer.youkon.viewmodels.QuickConvertViews

class QuickConvertCard(
    private val vm: QuickConvertCardViewModel = QuickConvertCardViewModel()
) {
    @Composable
    fun Body() {
        Surface(
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
            shape = MaterialTheme.shapes.medium,
            modifier = OnboardingModifier(QuickConvertViews.SURFACE)
                .width(420.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                CardLabel()
                ContentGrid()
            }
        }
    }

    /// Provides a view modifier for a colored shadow if the selected view is highlighted in the onboarding screen
    @Composable
    fun OnboardingModifier(view: QuickConvertViews): Modifier {
        return if (vm.highlightedView.value == view)
            Modifier.coloredShadow(animatedColor)
        else
            Modifier
    }

    /// The label at the top of the card
    @Composable
    private fun CardLabel() {
        Text("Quick Convert",
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge
        )
    }

    /// A 2x2 grid with from dropdown in the upper left, to dropdown in the upper right, editable value in the lower left, and converted value in the lower right
    @Composable
    private fun ContentGrid() {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FromDropdown(OnboardingModifier(QuickConvertViews.FROM).weight(1f))
            ToDropdown(OnboardingModifier(QuickConvertViews.TO).weight(1f))
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(OnboardingModifier(QuickConvertViews.VALUE).weight(1f))
            ConvertedText(OnboardingModifier(QuickConvertViews.CONVERTED).weight(1f))
        }
    }

    /// Selection for which type of unit to convert from
    @Composable
    private fun FromDropdown(modifier: Modifier) {
        val measurement by vm.measurement.collectAsState()
        UnitDropdown(
            unit = measurement.unit,
            availableUnits = vm.allUnits,
            headerText = "From",
            modifier = modifier,
            onClick = { vm.updateUnit(it) }
        ).Body()
    }

    /// Selection for which type of unit to convert to
    @Composable
    private fun ToDropdown(modifier: Modifier) {
        UnitDropdown(
            unit = vm.targetUnit,
            availableUnits = vm.equivalentUnits,
            headerText = "To",
            modifier = modifier,
            onClick = { vm.updateTargetUnit(it) }
        ).Body()
    }

    /// The field that takes the user input on the numeric value of the measurement
    @Composable
    private fun TextField(modifier: Modifier) {
        val measurement = vm.measurement.collectAsState()
        MeasurementTextField(
            initialText = measurement.value.value.toString(),
            unitText = measurement.value.unit.shortUnit,
            modifier = modifier,
            updateMeasurement = { vm.updateValue(it) }
        )
    }

    /// The display of the measurement after conversion
    @Composable
    private fun ConvertedText(modifier: Modifier) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.CenterStart
        ) {
            Text(vm.convertedText,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview
@Composable
fun QuickConvertCardPreview() {
    QuickConvertCard().Body()
}