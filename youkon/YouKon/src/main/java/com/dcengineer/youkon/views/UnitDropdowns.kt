package com.dcengineer.youkon.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dcengineer.youkon.YkUnit

class UnitDropdown(
    val unit: YkUnit,
    val availableUnits: Array<YkUnit>,
    val headerText: String? = null,
    val modifier: Modifier = Modifier,
    val onClick: (YkUnit?) -> Unit = {}
) {
    private val isExpanded = mutableStateOf(false)

    @Composable
    fun Body() {
        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalAlignment = Alignment.Start,
            modifier = modifier
        ) {
            headerText?.let {header ->
                Text(header,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(start = 16.dp),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Menu()
        }
    }

    @Composable
    fun Menu() {
        Button(
            shape = MaterialTheme.shapes.medium,
            onClick = { isExpanded.value = !isExpanded.value },
        ) {
            MenuButton()
            DropdownMenu(
                expanded = isExpanded.value,
                onDismissRequest = { isExpanded.value = false }
            ) {
                MenuItems()
            }
        }
    }

    @Composable
    fun MenuItems() {
        availableUnits.forEach { unit ->
            DropdownMenuItem(
                text = {
                    Text(unit.toString().replace("_", " "),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                onClick = {
                isExpanded.value = false
                onClick(unit)
                }
            )
        }
    }

    @Composable
    fun MenuButton() {
        Text(unit.toString().replace("_", " "),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.surface,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun UnitDropdownPreview() {
    UnitDropdown(YkUnit.METERS, YkUnit.METERS.allUnits).Body()
}