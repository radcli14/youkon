package com.dcsim.youkon.android.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dcsim.youkon.YkUnit

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
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 8.dp),
                    color = MaterialTheme.colors.primary
                )
            }
            Menu()
        }
    }

    @Composable
    fun Menu() {
        Button(
            onClick = { isExpanded.value = !isExpanded.value },
        ) {
            MenuButton()
            DropdownMenu(
                expanded = isExpanded.value,
                onDismissRequest = { onClick(null) }
            ) {
                MenuItems()
            }
        }
    }

    @Composable
    fun MenuItems() {
        availableUnits.forEach { unit ->
            DropdownMenuItem(onClick = {
                isExpanded.value = false
                onClick(unit)
            }) {
                Text(unit.toString().replace("_", " "))
            }
        }
    }

    @Composable
    fun MenuButton() {
        Text(unit.toString().replace("_", " "),
            style = MaterialTheme.typography.body1,
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