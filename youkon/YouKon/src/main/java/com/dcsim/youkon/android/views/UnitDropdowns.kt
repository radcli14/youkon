package com.dcsim.youkon.android.views

import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.dcsim.youkon.Measurement

@Composable
fun FromDropdown(measurement: Measurement) {
    var isExpanded by remember { mutableStateOf(false) }

    Button(onClick = { isExpanded = !isExpanded }) {
        Text("From")

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            measurement.allUnits.forEach {  unit ->
                DropdownMenuItem(onClick = { isExpanded = false }) {
                    Text(unit.toString())
                }
            }
        }
    }
}