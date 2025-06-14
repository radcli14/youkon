package view

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.composed
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import viewmodel.QuickConvertCardViewModel
import viewmodel.QuickConvertViews

class QuickConvertCard(
    private val vm: QuickConvertCardViewModel = QuickConvertCardViewModel()
) {
    private val tag = "QuickConvertCard"

    @Composable
    fun Body() {
        Surface(
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .onboardingModifier(QuickConvertViews.SURFACE)
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
    private fun Modifier.onboardingModifier(view: QuickConvertViews): Modifier = composed {
        this.onboardingModifier(vm.highlightedView.value == view)
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
            FromDropdown(
                modifier = Modifier
                    .onboardingModifier(QuickConvertViews.FROM)
                    .weight(1f)
            )
            ToDropdown(
                modifier = Modifier
                    .onboardingModifier(QuickConvertViews.TO)
                    .weight(1f)
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            TextField(modifier = Modifier.onboardingModifier(QuickConvertViews.VALUE))
            /*TextField(
                modifier = Modifier
                    .onboardingModifier(QuickConvertViews.VALUE)
                    .weight(1f)
                    .alignByBaseline()
            )
            ConvertedText(
                modifier = Modifier
                    .onboardingModifier(QuickConvertViews.CONVERTED)
                    .weight(1f)
                    .alignByBaseline()
            )*/
        }
    }

    /// Selection for which type of unit to convert from
    @Composable
    private fun FromDropdown(modifier: Modifier) {
        val data by vm.data.collectAsState()
        UnitDropdown(
            unit = data.unit,
            availableUnits = vm.allUnits,
            headerText = "From",
            isNested = true,
            modifier = modifier,
            onClick = { vm.updateUnit(it) }
        ).Body()
    }

    /// Selection for which type of unit to convert to
    @Composable
    private fun ToDropdown(modifier: Modifier) {
        val data by vm.data.collectAsState()
        UnitDropdown(
            unit = data.targetUnit,
            availableUnits = data.equivalentUnits,
            headerText = "To",
            modifier = modifier,
            onClick = { vm.updateTargetUnit(it) }
        ).Body()
    }

    /// The field that takes the user input on the numeric value of the measurement
    @Composable
    private fun TextField(modifier: Modifier) {
        val data by vm.data.collectAsState()
        MeasurementTextField(
            initialText = data.value.toString(),
            unitText = data.unit.shortUnit,
            modifier = modifier,
            updateMeasurement = { vm.updateValue(it) },
            alignedContent = { alignedModifier ->
                ConvertedText(alignedModifier.onboardingModifier(QuickConvertViews.CONVERTED))
            }
        )
    }

    /// The display of the measurement after conversion
    @Composable
    private fun ConvertedText(modifier: Modifier) {
        val data by vm.data.collectAsState()
        val isOneLine by vm.convertedTextFitsOnOneLine.collectAsState()

        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp),
            modifier = modifier,
        ) {
            if (isOneLine) {
                // Initially try a one line representation, and detect its layout size.
                // If it fits one one line, then we will stay with this.
                TextWithSubscripts(data.convertedText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    onTextLayout = vm::handleConvertedTextLayout
                )
            } else {
                // If everything is too large, place the name of the converted units on the second line
                Text(data.convertedText.substringBefore(data.targetUnit.shortUnit),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                // TODO: currently this looks good on Android, but leaves too much space on iPhone
                TextWithSubscripts("      ${data.targetUnit.shortUnit}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
