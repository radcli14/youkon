package com.dcsim.youkon.android.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dcsim.youkon.android.viewmodels.QuickConvertCardViewModel

class QuickConvertCard {
    private val vm = QuickConvertCardViewModel()

    @Composable
    fun Body() {
        Surface(
            color = MaterialTheme.colors.surface.copy(alpha = 0.4f),
            modifier = Modifier
                .requiredWidth(360.dp)
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
            style = MaterialTheme.typography.h6
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
        val unit = remember { mutableStateOf(vm.measurement.unit) }
        val availableUnits = remember { mutableStateOf(unit.value.allUnits) }
        UnitDropdown(
            unit = unit.value,
            availableUnits = availableUnits.value,
            headerText = "From"
        ).Body()
    }

    /// Selection for which type of unit to convert to
    @Composable
    private fun ToDropdown() {
        val unit = remember { mutableStateOf(vm.targetUnit) }
        val availableUnits = remember { mutableStateOf(vm.targetUnit.equivalentUnits()) }
        UnitDropdown(
            unit = unit.value,
            availableUnits = availableUnits.value,
            headerText = "To"
        ).Body()
    }

    /// The field that takes the user input on the numeric value of the measurement
    @Composable
    private fun TextField() {
        MeasurementTextField(measurement = vm.measurement) {
            vm.setConvertedText()
        }
    }

    /// The display of the measurement after conversion
    @Composable
    private fun ConvertedText() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 15.dp)
        ) {
            Text(vm.convertedText,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }

    }
}

@Preview
@Composable
fun QuickConvertCardPreview() {
    QuickConvertCard().Body()
}