package com.dcsim.youkon.android.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dcsim.youkon.YkMeasurement
import com.dcsim.youkon.YkProject
import com.dcsim.youkon.ProjectExpansionLevel
import com.dcsim.youkon.YkSystem
import com.dcsim.youkon.android.viewmodels.ProjectViewModel

//@Composable
class ProjectView(
    val vm: ProjectViewModel
) {
    @Composable
    fun Body() {
        var expansion by remember { mutableStateOf(ProjectExpansionLevel.COMPACT) }
        var measurements by remember { mutableStateOf(vm.project.measurements) }

        Card(
            modifier = Modifier
            .clickable
            {
                expansion = when (expansion) {
                    ProjectExpansionLevel.COMPACT -> ProjectExpansionLevel.STATIC
                    ProjectExpansionLevel.STATIC -> ProjectExpansionLevel.COMPACT
                    ProjectExpansionLevel.EDITABLE -> ProjectExpansionLevel.STATIC
                }
            }
            .padding(8.dp)
        )
        {
            Column(modifier = Modifier.padding(16.dp)) {
                ProjectTopRow(vm.project, expansion)
                ProjectContent(measurements, expansion,
                    editClick = {
                        expansion = when (expansion) {
                            ProjectExpansionLevel.STATIC -> ProjectExpansionLevel.EDITABLE
                            else -> ProjectExpansionLevel.STATIC
                        }
                    },
                    addClick = {
                        println("newNewNew")
                        vm.project.measurements.add(
                            YkMeasurement.new()
                        )
                        measurements = vm.project.measurements
                        println(measurements)
                    }
                )
            }
        }
    }
}


/// Top row with the name and description of the project, and button to expand/close
@Composable
fun ProjectTopRow(project: YkProject, expansion: ProjectExpansionLevel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            NameText(project.name)
            DescriptionText(project.about)
        }
        CloseIcon(expansion)
    }
}


/// Content with either static or editable measurements
@Composable
fun ProjectContent(
    measurements: List<YkMeasurement>,
    expansion: ProjectExpansionLevel,
    editClick: () -> Unit,
    addClick: () -> Unit
) {
    Row {
        Column {
            if (expansion == ProjectExpansionLevel.EDITABLE) {
                // Editable fields for each measurement and unit selection
                measurements.forEach { measurement ->
                    MeasurementView(measurement = measurement)
                }
            } else if (expansion == ProjectExpansionLevel.STATIC) {
                // Displays of the measurement after conversion to a consistent set of units
                measurements.forEach { measurement ->
                    Text(measurement.nameAndValueInSystem(YkSystem.SI))
                }
            }
        }

        // The edit button is visible when the project has been expanded beyond its compact level.
        // When it is tapped, the value, units, name, and description fields can be edited.
        // Tap again to collapse to the static level, in which you see but don't modify measurements.
        if (expansion != ProjectExpansionLevel.COMPACT) {
            Spacer(Modifier.weight(1f))
            Card {
                Column {
                    EditButton { editClick() }
                    if (expansion == ProjectExpansionLevel.EDITABLE) {
                        PlusButton { addClick() }
                        MinusButton { }
                    }
                }
            }
        }
    }
}
