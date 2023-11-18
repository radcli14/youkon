package com.dcsim.youkon.android.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.dcsim.youkon.android.viewmodels.QuickConvertCardViewModel

class QuickConvertCard(
    private val vm: QuickConvertCardViewModel = QuickConvertCardViewModel()
) {
    @Composable
    fun Body() {
        Surface(
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
            shape = RoundedCornerShape(roundedRadius),
            modifier = Modifier
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

    /// The label at the top of the card
    @Composable
    private fun CardLabel() {
        Text("Quick Convert",
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge
        )
    }

    @Composable
    private fun ContentGrid() {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FromDropdown(Modifier.weight(1f))
            ToDropdown(Modifier.weight(1f))
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(Modifier.weight(1f))
            ConvertedText(Modifier.weight(1f))
        }
        /*LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                FromDropdown()
            }
            item {
                ToDropdown()
            }
            item {
                TextField()
            }
            item {
                ConvertedText()
            }
        }*/
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
            modifier = modifier,
            updateMeasurement = { vm.updateValue(it) }
        )
    }

    /// The display of the measurement after conversion
    @Composable
    private fun ConvertedText(modifier: Modifier) {
        Box(
            modifier = modifier,
                //.fillMaxSize()
                //.padding(top = 3.dp),
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