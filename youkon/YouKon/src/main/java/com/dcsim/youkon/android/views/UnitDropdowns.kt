package com.dcsim.youkon.android.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dcsim.youkon.Measurement

@Composable
fun FromDropdown(measurement: Measurement) {
    val isExpanded = remember { mutableStateOf(false) }

    Button(
        onClick = { isExpanded.value = !isExpanded.value },
        modifier = Modifier.width(112.dp)
    ) {
        UnitDropdownButtonColumn(firstLine = "From", secondLine = measurement.unit.toString())
        UnitDropdownMenuItems(units = measurement.allUnits, isExpanded = isExpanded)
    }
}

@Composable
fun ToDropdown(measurement: Measurement, targetUnit: String) {
    val isExpanded = remember { mutableStateOf(false) }

    Button(
        onClick = { isExpanded.value = !isExpanded.value },
        modifier = Modifier.width(112.dp)
    ) {
        UnitDropdownButtonColumn(firstLine = "To", secondLine = targetUnit)
        UnitDropdownMenuItems(units = measurement.equivalentUnits(), isExpanded = isExpanded)
    }
}

@Composable
fun UnitDropdownButtonColumn(firstLine: String, secondLine: String) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(firstLine)
        Text(secondLine,
            style = MaterialTheme.typography.caption
        )
    }
}

@Composable
fun UnitDropdownMenuItems(units: Array<Measurement.Unit>, isExpanded: MutableState<Boolean>) {
    DropdownMenu(
        expanded = isExpanded.value,
        onDismissRequest = { isExpanded.value = false }
    ) {
        units.forEach {  unit ->
            DropdownMenuItem(onClick = { isExpanded.value = false }) {
                Text(unit.toString())
            }
        }
    }
}