package com.dcsim.youkon.android.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
                verticalArrangement = Arrangement.spacedBy(8.dp)
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
            style = MaterialTheme.typography.titleLarge
        )
    }

    @Composable
    private fun ContentGrid() {
        LazyVerticalGrid(
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
        }
    }

    /// Selection for which type of unit to convert from
    @Composable
    private fun FromDropdown() {
        val measurement by vm.measurement.collectAsState()
        UnitDropdown(
            unit = measurement.unit,
            availableUnits = vm.allUnits,
            headerText = "From",
            onClick = { vm.updateUnit(it) }
        ).Body()
    }

    /// Selection for which type of unit to convert to
    @Composable
    private fun ToDropdown() {
        UnitDropdown(
            unit = vm.targetUnit,
            availableUnits = vm.equivalentUnits,
            headerText = "To",
            onClick = { vm.updateTargetUnit(it) }
        ).Body()
    }

    /// The field that takes the user input on the numeric value of the measurement
    @Composable
    private fun TextField() {
        val measurement = vm.measurement.collectAsState()
        MeasurementTextField(
            initialText = measurement.value.value.toString(),
            updateMeasurement = { vm.updateValue(it) }
        )
    }

    /// The display of the measurement after conversion
    @Composable
    private fun ConvertedText() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 3.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(vm.convertedText,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Preview
@Composable
fun QuickConvertCardPreview() {
    QuickConvertCard().Body()
}