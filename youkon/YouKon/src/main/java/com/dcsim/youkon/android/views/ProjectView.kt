package com.dcsim.youkon.android.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
                Text(
                    text = project.name,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.SemiBold
                )
                if (isExpanded) {
                    IconButton(
                        onClick = { isExpanded = false }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
            }

            if (isExpanded) {
                project.measurements.forEach { measurement ->
                    MeasurementView(measurement = measurement)
                }
            } else {
                Text(
                    text = project.description,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
