package view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import model.YkType
import model.YkUnit
import org.jetbrains.compose.resources.stringResource
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.choose_unit
import youkon.composeapp.generated.resources.choose_unit_type
import youkon.composeapp.generated.resources.want_more

class UnitDropdown(
    val unit: YkUnit,
    val availableUnits: Array<YkUnit>,
    val headerText: String? = null,
    val isNested: Boolean = false,
    val isExtended: Boolean = false,
    val includeUnitless: Boolean = false,
    val modifier: Modifier = Modifier,
    val onClick: (YkUnit?) -> Unit = {},
    val onShowPaywall: () -> Unit = {}
) {
    private val isExpanded = mutableStateOf(false)
    private val typeIsExpanded = mutableStateOf(false)
    private var selectedType: YkType? = null
    private val headerPadding = 8.dp
    private val contentPadding = 12.dp

    @Composable
    fun Body() {
        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalAlignment = Alignment.Start,
            modifier = modifier
        ) {
            headerText?.let { header ->
                HeaderText(header)
            }
            Menu()
        }
    }

    /// This is a formatted text that appears above the content, either the `From` or `To` header
    /// in the `QuickConvertCard`, or instructions in a nested menu.
    @Composable
    fun HeaderText(text: String) {
        Text(text,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = headerPadding),
            color = MaterialTheme.colorScheme.secondary
        )
    }

    /// Displays a button with text of the currently selected unit, which opens a dropdown when tapped
    @Composable
    fun Menu() {
        Button(
            shape = MaterialTheme.shapes.medium,
            onClick = { isExpanded.value = !isExpanded.value },
            contentPadding = PaddingValues(horizontal = contentPadding)
        ) {
            MenuButton()

            // The main dropdown
            DropdownMenu(
                expanded = isExpanded.value,
                onDismissRequest = { isExpanded.value = false }
            ) {
                if (isNested) {
                    TypeMenuItems()
                } else {
                    UnitMenuItems(availableUnits)
                }

                if (!isExtended) {
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.want_more)) },
                        onClick = {
                            isExpanded.value = false
                            onShowPaywall()
                        },
                        trailingIcon = {
                            Icon(Icons.TwoTone.WorkspacePremium, contentDescription = null)
                        },
                        colors = MenuDefaults.itemColors(
                            textColor = MaterialTheme.colorScheme.primary,
                            trailingIconColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            // The nested dropdown
            DropdownMenu(
                expanded = typeIsExpanded.value,
                offset = DpOffset(contentPadding, contentPadding),
                onDismissRequest = {
                    typeIsExpanded.value = false
                    selectedType = null
                }
            ) {
                selectedType?.let {
                    HeaderText(stringResource(Res.string.choose_unit, it.lowercasedString))
                    UnitMenuItems(it.units.filter { availableUnits.contains(it) }.toTypedArray())
                }
            }
        }
    }

    /// If the nested option is used, this initially opens a menu with a list of unit types
    // (Mass, Length, ...), rather than the list of all existing units
    @Composable
    fun TypeMenuItems() {
        HeaderText(stringResource(Res.string.choose_unit_type))

        if (includeUnitless) {
            DropdownMenuItem(
                text = {
                    Text(
                        YkUnit.UNITLESS.lowercasedString,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                onClick = {
                    isExpanded.value = false
                    onClick(YkUnit.UNITLESS)
                }
            )
        }

        val types: Array<YkType> = if (isExtended) YkType.entries.toTypedArray() else YkType.MASS.basicTypes
        types.forEach { unitType ->
            DropdownMenuItem(
                text = {
                    Text(
                        unitType.lowercasedString,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                onClick = {
                    typeIsExpanded.value = !typeIsExpanded.value
                    selectedType = unitType
                }
            )
        }
    }

    /// This displays a list of all units of a given type, when tapped, that unit is selected
    @Composable
    fun UnitMenuItems(units: Array<YkUnit>) {
        units.forEach { unit ->
            DropdownMenuItem(
                text = {
                    Text(
                        unit.lowercasedString,
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

    /// This is the text of the menu button that is visible even when the dropdown is not expanded
    @Composable
    fun MenuButton() {
        Text(
            unit.lowercasedString,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
