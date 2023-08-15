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
import com.dcsim.youkon.Project

enum class ProjectExpansionLevel {
    COMPACT, STATIC, EDITABLE
}

@Composable
fun ProjectView(project: Project) {
    var expansion by remember { mutableStateOf(ProjectExpansionLevel.COMPACT) }

    Card(
        modifier = Modifier
            .clickable {
                expansion = when (expansion) {
                    ProjectExpansionLevel.COMPACT -> ProjectExpansionLevel.STATIC
                    ProjectExpansionLevel.STATIC -> ProjectExpansionLevel.COMPACT
                    ProjectExpansionLevel.EDITABLE -> ProjectExpansionLevel.STATIC
                }
            }
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            ProjectTopRow(project, expansion)
            ProjectContent(project, expansion) {
                expansion = when(expansion) {
                    ProjectExpansionLevel.STATIC -> ProjectExpansionLevel.EDITABLE
                    else -> ProjectExpansionLevel.STATIC
                }
            }
        }
    }
}


/// Top row with the name and description of the project, and button to expand/close
@Composable
fun ProjectTopRow(project: Project, expansion: ProjectExpansionLevel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            NameText(project.name)
            DescriptionText(project.description)
        }
        CloseIcon(expansion)
    }
}


/// Content with either static or editable measurements
@Composable
fun ProjectContent(project: Project, expansion: ProjectExpansionLevel, onClick: () -> Unit) {
    Row {
        Column {
            if (expansion == ProjectExpansionLevel.EDITABLE) {
                // Editable fields for each measurement and unit selection
                project.measurements.forEach { measurement ->
                    MeasurementView(measurement = measurement)
                }
            } else if (expansion == ProjectExpansionLevel.STATIC) {
                // Displays of the measurement after conversion to a consistent set of units
                project.measurements.forEach { measurement ->
                    Text(measurement.nameAndValueInSystem("SI"))
                }
            }
        }

        // The edit button is visible when the project has been expanded beyond its compact level.
        // When it is tapped, the value, units, name, and description fields can be edited.
        // Tap again to collapse to the static level, in which you see but don't modify measurements.
        if (expansion != ProjectExpansionLevel.COMPACT) {
            Spacer(Modifier.weight(1f))
            Column {
                EditButton { onClick() }
                if (expansion == ProjectExpansionLevel.EDITABLE) {
                    PlusButton { }
                    MinusButton { }
                }
            }
        }
    }
}
