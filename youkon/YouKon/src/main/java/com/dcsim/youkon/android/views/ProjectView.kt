package com.dcsim.youkon.android.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dcsim.youkon.Project

@Composable
fun ProjectView(project: Project) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .clickable { isExpanded = true }
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NameText(project.name)
                if (isExpanded) {
                    CloseButton(onClick = { isExpanded = false })
                }
            }

            if (isExpanded) {
                project.measurements.forEach { measurement ->
                    MeasurementView(measurement = measurement)
                }
            } else {
                DescriptionText(project.description)
            }
        }
    }
}
