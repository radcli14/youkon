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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dcsim.youkon.Measurement

@Composable
fun FromDropdown(measurement: Measurement) {
    val isExpanded = remember { mutableStateOf(false) }
    var unitText by remember { mutableStateOf(measurement.unit.toString()) }

    Button(
        onClick = { isExpanded.value = !isExpanded.value },
        modifier = Modifier.width(112.dp)
    ) {
        UnitDropdownButtonColumn(firstLine = "From", secondLine = unitText)
        UnitDropdownMenuItems(
            units = measurement.allUnits,
            isExpanded = isExpanded
        ) { unit ->
            isExpanded.value = false
            if (unit != null) {
                measurement.unit = unit
            }
            unitText = unit.toString()
        }
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
        UnitDropdownMenuItems(
            units = measurement.equivalentUnits(),
            isExpanded = isExpanded
        ) { unit ->
            isExpanded.value = false
        }
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
fun UnitDropdownMenuItems(units: Array<Measurement.Unit>, isExpanded: MutableState<Boolean>, onClick: (Measurement.Unit?) -> Unit) {
    DropdownMenu(
        expanded = isExpanded.value,
        onDismissRequest = { onClick(null) }
    ) {
        units.forEach {  unit ->
            DropdownMenuItem(onClick = { onClick(unit) }) {
                Text(unit.toString())
            }
        }
    }
}