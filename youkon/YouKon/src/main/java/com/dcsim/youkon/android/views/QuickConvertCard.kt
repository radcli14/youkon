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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dcsim.youkon.android.viewmodels.QuickConvertCardViewModel

class QuickConvertCard {
    val vm = QuickConvertCardViewModel()

    @Composable
    fun Body() {
        Card(
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
    fun CardLabel() {
        Text("Quick Convert",
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.h6
        )
    }

    @Composable
    fun ContentGrid() {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            // Selection for which type of unit to convert from
            FromDropdown(measurement = vm.measurement) { unit ->
                vm.updateUnit(unit)
            }
            ToDropdown(equivalentUnits = vm.equivalentUnits, targetUnit = vm.targetUnit)
        }

        Row {
            // The field that takes the user input on the numeric value of the measurement
            MeasurementTextField(measurement = vm.measurement) {
                vm.convertedText = vm.measurement.valueAndConversion(vm.targetUnit)
            }

            // The display of the measurement after conversion
            Text(vm.convertedText,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(top = 8.dp, start = 16.dp)
            )
        }
    }
}
