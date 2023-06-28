package com.dcsim.youkon.android.views

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dcsim.youkon.Measurement
import com.dcsim.youkon.Project

@Composable
fun ProjectsCard(projects: List<Project>) {
    Card(modifier = Modifier.padding(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Projects",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn {
                items(projects) { project ->
                    ProjectView(project = project)
                }
            }
        }
    }
}


@Composable
fun ProjectView(project: Project) {
    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .clickable { isExpanded = true }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = project.name,
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = project.description,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(top = 8.dp)
            )

            if (isExpanded) {
                AlertDialog(
                    onDismissRequest = { isExpanded = false },
                    buttons = {},
                    title = {},
                    text = {
                        Column {
                            project.measurements.forEach { measurement ->
                                MeasurementView(measurement = measurement)
                            }
                        }
                    }
                )
            }

            Text(
                text = if (isExpanded) "Collapse" else "Expand",
                style = MaterialTheme.typography.button,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}


@Composable
fun MeasurementView(measurement: Measurement) {
    var editedName by remember { mutableStateOf(measurement.name) }
    var editedDescription by remember { mutableStateOf(measurement.description) }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        TextField(
            value = editedName,
            onValueChange = { editedName = it },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.subtitle1
        )
        TextField(
            value = editedDescription,
            onValueChange = { editedDescription = it },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.body1
        )

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
            TextField(
                value = measurement.value.toString(),
                onValueChange = { newValue ->
                    val parsedValue = newValue.toDoubleOrNull()
                    if (parsedValue != null) {
                        measurement.value = parsedValue
                    }
                },
                modifier = Modifier.width(96.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            FromDropdown(measurement = measurement) { unit ->
                measurement.unit = unit
            }
        }
    }

    // Update the measurement with edited name and description when focus is lost
    DisposableEffect(Unit) {
        onDispose {
            measurement.name = editedName
            measurement.description = editedDescription
        }
    }
}


