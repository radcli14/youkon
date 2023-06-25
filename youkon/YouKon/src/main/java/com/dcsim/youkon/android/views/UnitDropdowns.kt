package com.dcsim.youkon.android.views

import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.dcsim.youkon.Measurement

@Composable
fun FromDropdown(measurement: Measurement) {
    val isExpanded = remember { mutableStateOf(false) }

    Button(onClick = { isExpanded.value = !isExpanded.value }) {
        Text("From")
        UnitDropdownMenuItems(units = measurement.allUnits, isExpanded = isExpanded)
    }
}

@Composable
fun ToDropdown(measurement: Measurement) {
    val isExpanded = remember { mutableStateOf(false) }

    Button(onClick = { isExpanded.value = !isExpanded.value }) {
        Text("To")
        UnitDropdownMenuItems(units = measurement.equivalentUnits(), isExpanded = isExpanded)
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